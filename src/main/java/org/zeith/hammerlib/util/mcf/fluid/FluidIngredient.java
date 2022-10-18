package org.zeith.hammerlib.util.mcf.fluid;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.IReverseTag;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record FluidIngredient(CompareMode mode, List<FluidStack> asFluidStack, List<TagKey<Fluid>> asTags)
		implements Predicate<FluidStack>
{
	public static final Codec<FluidIngredient> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.STRING.fieldOf("mode").xmap(CompareMode::valueOf, CompareMode::name).forGetter(FluidIngredient::mode),
					FluidStack.CODEC.listOf().fieldOf("fluids").forGetter(FluidIngredient::asFluidStack),
					TagKey.codec(ForgeRegistries.Keys.FLUIDS).listOf().fieldOf("tags").forGetter(FluidIngredient::asTags)
			).apply(instance, FluidIngredient::new)
	);
	
	public static FluidIngredient fromJson(JsonElement json)
	{
		return JsonOps.INSTANCE.withDecoder(CODEC).apply(json).result().orElseThrow().getFirst();
	}
	
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
	
	public JsonElement toJson()
	{
		return JsonOps.INSTANCE.withEncoder(CODEC).apply(this).result().orElseThrow();
	}
	
	FluidIngredient resolve()
	{
		return isEmpty() ? EMPTY : this;
	}
	
	public boolean isEmpty()
	{
		return this == EMPTY || (asFluidStack.isEmpty() && asTags().isEmpty());
	}
	
	@Override
	public boolean test(FluidStack fluidStack)
	{
		if(isEmpty())
			return fluidStack.isEmpty();
		
		return switch(mode)
				{
					case BOTH -> asFluidStack.stream().anyMatch(fluidStack::isFluidEqual)
							|| ForgeRegistries.FLUIDS.tags().getReverseTag(fluidStack.getFluid())
							.stream()
							.flatMap(IReverseTag::getTagKeys)
							.anyMatch(asTags::contains);
					
					case VALUES -> asFluidStack.stream().anyMatch(fluidStack::isFluidEqual);
					
					case TAGS -> ForgeRegistries.FLUIDS.tags().getReverseTag(fluidStack.getFluid())
							.stream()
							.flatMap(IReverseTag::getTagKeys)
							.anyMatch(asTags::contains);
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
							Optional.ofNullable(ForgeRegistries.FLUIDS.tags())
									.stream()
									.flatMap(manager -> asTags.stream().map(manager::getTag))
									.flatMap(tag -> tag.stream().map(f -> new FluidStack(f, amount))),
							asFluidStack.stream()
									.map(fs -> FluidHelper.withAmount(fs, amount))
					).toArray(FluidStack[]::new);
					
					case TAGS -> Optional.ofNullable(ForgeRegistries.FLUIDS.tags())
							.stream()
							.flatMap(manager -> asTags.stream().map(manager::getTag))
							.flatMap(tag -> tag.stream().map(f -> new FluidStack(f, amount)))
							.toArray(FluidStack[]::new);
					
					case VALUES -> asFluidStack.stream()
							.map(fs -> FluidHelper.withAmount(fs, amount))
							.toArray(FluidStack[]::new);
				};
	}
	
	public enum CompareMode
	{
		TAGS,
		VALUES,
		BOTH;
	}
}