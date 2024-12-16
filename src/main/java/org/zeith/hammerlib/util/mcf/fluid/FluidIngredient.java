package org.zeith.hammerlib.util.mcf.fluid;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public record FluidIngredient(CompareMode mode, List<FluidStack> asFluidStack, List<TagKey<Fluid>> asTags)
		implements Predicate<FluidStack>
{
	public static final MapCodec<FluidIngredient> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(
					Codec.STRING.fieldOf("mode").xmap(CompareMode::valueOf, CompareMode::name).forGetter(FluidIngredient::mode),
					FluidStack.CODEC.listOf().fieldOf("fluids").forGetter(FluidIngredient::asFluidStack),
					TagKey.codec(BuiltInRegistries.FLUID.key()).listOf().fieldOf("tags").forGetter(FluidIngredient::asTags)
			).apply(instance, FluidIngredient::new)
	);
	
	public static final StreamCodec<RegistryFriendlyByteBuf, FluidIngredient> STREAM_CODEC = StreamCodec.of(
			(buf, ing) -> ing.toNetwork(buf),
			FluidIngredient::fromNetwork
	);
	
	public static FluidIngredient EMPTY = new FluidIngredient(CompareMode.VALUES, List.of(), List.of());
	
	public static FluidIngredient ofTags(List<TagKey<Fluid>> tags)
	{
		return new FluidIngredient(CompareMode.TAGS, List.of(), tags).resolve();
	}
	
	public static FluidIngredient ofFluids(List<FluidStack> fluids)
	{
		return new FluidIngredient(CompareMode.VALUES, fluids, List.of()).resolve();
	}
	
	/**
	 * Concatenates all ingredients of the given array into a new ingredient that matches if any of the given ingredients match.
	 */
	public static FluidIngredient join(FluidIngredient... ingredients)
	{
		List<FluidStack> asFluidStack = new ArrayList<>();
		List<TagKey<Fluid>> asTags = new ArrayList<>();
		
		for(FluidIngredient ingredient : ingredients)
		{
			asTags.addAll(ingredient.asTags);
			asFluidStack.addAll(ingredient.asFluidStack);
		}
		
		if(asFluidStack.isEmpty())
		{
			if(asTags.isEmpty())
				return EMPTY;
			
			return FluidIngredient.ofTags(asTags);
		}
		
		if(asTags.isEmpty())
			return FluidIngredient.ofFluids(asFluidStack);
		
		return new FluidIngredient(CompareMode.BOTH, asFluidStack, asTags);
	}
	
	public FluidIngredient(CompareMode mode, List<FluidStack> asFluidStack, List<TagKey<Fluid>> asTags)
	{
		this.mode = mode;
		this.asFluidStack = asFluidStack.stream().map(fs -> FluidHelper.withAmount(fs, 1)).toList();
		this.asTags = asTags;
	}
	
	FluidIngredient resolve()
	{
		return isEmpty() ? EMPTY : this;
	}
	
	public boolean isEmpty()
	{
		return this == EMPTY || (asFluidStack.isEmpty() && asTags().isEmpty());
	}
	
	public FluidIngredientStack stack(int amount)
	{
		return new FluidIngredientStack(this, amount);
	}
	
	@Override
	public boolean test(FluidStack fluidStack)
	{
		if(isEmpty())
			return fluidStack.isEmpty();
		
		return switch(mode)
		{
			case BOTH -> asFluidStack.stream().anyMatch(fs -> FluidStack.isSameFluidSameComponents(fluidStack, fs))
						 || asTags.stream().map(BuiltInRegistries.FLUID::getTagOrEmpty)
								 .flatMap(holders -> StreamSupport.stream(holders.spliterator(), false))
								 .filter(Holder::isBound)
								 .map(Holder::value)
								 .anyMatch(fluidStack.getFluid()::equals);
			
			case VALUES -> asFluidStack.stream().anyMatch(fs -> FluidStack.isSameFluidSameComponents(fluidStack, fs));
			
			case TAGS -> asTags.stream().map(BuiltInRegistries.FLUID::getTagOrEmpty)
					.flatMap(holders -> StreamSupport.stream(holders.spliterator(), false))
					.filter(Holder::isBound)
					.map(Holder::value)
					.anyMatch(fluidStack.getFluid()::equals);
		};
	}
	
	public FluidStack[] getValues()
	{
		return getValues(1);
	}
	
	public FluidStack[] getValues(int amount)
	{
		return switch(mode)
		{
			case BOTH -> Stream.concat(
					asTags.stream().map(BuiltInRegistries.FLUID::getTagOrEmpty)
							.flatMap(holders -> StreamSupport.stream(holders.spliterator(), false))
							.map(Holder::value)
							.map(f -> new FluidStack(f, amount)),
					asFluidStack.stream()
							.map(fs -> FluidHelper.withAmount(fs, amount))
			).toArray(FluidStack[]::new);
			
			case TAGS -> asTags.stream().map(BuiltInRegistries.FLUID::getTagOrEmpty)
					.flatMap(holders -> StreamSupport.stream(holders.spliterator(), false))
					.map(Holder::value)
					.map(f -> new FluidStack(f, amount))
					.toArray(FluidStack[]::new);
			
			case VALUES -> asFluidStack.stream()
					.map(fs -> FluidHelper.withAmount(fs, amount))
					.toArray(FluidStack[]::new);
		};
	}
	
	public static FluidIngredient fromNetwork(RegistryFriendlyByteBuf buf)
	{
		CompareMode mode = buf.readEnum(CompareMode.class);
		
		int cap = buf.readVarInt();
		List<FluidStack> stacks = Lists.newArrayListWithCapacity(cap);
		for(int i = 0; i < cap; i++) stacks.add(FluidStack.STREAM_CODEC.decode(buf));
		
		cap = buf.readVarInt();
		List<TagKey<Fluid>> tags = Lists.newArrayListWithCapacity(cap);
		for(int i = 0; i < cap; i++) tags.add(TagKey.create(Registries.FLUID, buf.readResourceLocation()));
		
		return new FluidIngredient(mode, stacks, tags);
	}
	
	public void toNetwork(RegistryFriendlyByteBuf buf)
	{
		buf.writeEnum(mode());
		
	}
	
	public enum CompareMode
	{
		TAGS,
		VALUES,
		BOTH;
	}
}