package org.zeith.hammerlib.api.fluid;

import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.*;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.fml.*;
import org.zeith.hammerlib.core.adapter.TagAdapter;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.functions.Function2;
import org.zeith.hammerlib.util.mcf.fluid.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;

public class FluidFactory
		implements ICustomRegistrar, ItemLike
{
	public final FluidType type;
	public final Supplier<Item> bucket;
	public final ForgeFlowingFluid.Properties fluidProps;
	public final Supplier<ForgeFlowingFluid.Source> source;
	public final Supplier<ForgeFlowingFluid.Flowing> flowing;
	public final Supplier<LiquidBlock> block;
	protected Supplier<Supplier<RenderType>> renderType = () -> RenderType::solid;
	protected final List<TagKey<Fluid>> fluidTags = Lists.newArrayList();
	protected final List<CreativeModeTab> tabs = Lists.newArrayList();
	
	public FluidFactory(
			Supplier<FluidType> typeGenerator,
			Function2<Supplier<FlowingFluid>, List<CreativeModeTab>, Item> bucket,
			UnaryOperator<ForgeFlowingFluid.Properties> propertyModifier,
			Function<Supplier<? extends FlowingFluid>, LiquidBlock> block,
			Function<ForgeFlowingFluid.Properties, ForgeFlowingFluid.Source> sourceGen,
			Function<ForgeFlowingFluid.Properties, ForgeFlowingFluid.Flowing> flowingGen
	)
	{
		this.type = typeGenerator.get();
		
		this.block = block != null ? Suppliers.memoize(() -> block.apply(this::getFlowing)) : null;
		this.bucket = bucket != null ? Suppliers.memoize(() -> bucket.apply(this::getSource, tabs)) : null;
		
		var fp = new ForgeFlowingFluid.Properties(this::getType, this::getSource, this::getFlowing);
		
		if(this.block != null) fp = fp.block(this::getBlock);
		if(this.bucket != null) fp = fp.bucket(this::getBucket);
		
		this.fluidProps = propertyModifier != null ? propertyModifier.apply(fp) : fp;
		
		this.source = Suppliers.memoize(() -> sourceGen.apply(this.fluidProps));
		this.flowing = Suppliers.memoize(() -> flowingGen.apply(this.fluidProps));
	}
	
	protected FluidFactory addFluidTags(Collection<TagKey<Fluid>> fluidTags)
	{
		this.fluidTags.addAll(fluidTags);
		return this;
	}
	
	protected FluidFactory withRenderType(Supplier<Supplier<RenderType>> renderType)
	{
		this.renderType = renderType;
		return this;
	}
	
	protected FluidFactory addToTabs(Collection<CreativeModeTab> tab)
	{
		tabs.addAll(tab);
		return this;
	}
	
	public FluidStack stack(int amount)
	{
		return new FluidStack(this.source.get(), amount);
	}
	
	public FluidIngredient ingredient()
	{
		return FluidIngredient.ofFluids(List.of(new FluidStack(this.source.get(), 1)));
	}
	
	public FluidIngredientStack ingredient(int amount)
	{
		return new FluidIngredientStack(ingredient(), amount);
	}
	
	public FluidType getType()
	{
		return type;
	}
	
	public ForgeFlowingFluid.Source getSource()
	{
		return source.get();
	}
	
	public BlockState getSourceBlockState()
	{
		return source.get().getSource(false).createLegacyBlock();
	}
	
	public ForgeFlowingFluid.Flowing getFlowing()
	{
		return flowing.get();
	}
	
	@Nullable
	public Item getBucket()
	{
		return bucket != null ? bucket.get() : null;
	}
	
	@Nullable
	public LiquidBlock getBlock()
	{
		return block != null ? block.get() : null;
	}
	
	public ResourceLocation subId(ResourceLocation fluidId, String thing)
	{
		return new ResourceLocation(fluidId.getNamespace(), fluidId.getPath() + "_" + thing);
	}
	
	public boolean is(Fluid fluid)
	{
		return fluid == source || fluid == flowing;
	}
	
	public boolean is(FluidType fluid)
	{
		return fluid == type;
	}
	
	public boolean is(FluidStack fluid)
	{
		return fluid != null && !fluid.isEmpty() && is(fluid.getFluid());
	}
	
	public boolean is(Block fluid)
	{
		return fluid == getBlock();
	}
	
	public boolean is(Item bucket)
	{
		return bucket == getBucket();
	}
	
	public boolean has(ItemStack stack)
	{
		return !stack.isEmpty() &&
				FluidUtil.getFluidHandler(stack)
						.resolve()
						.map(h ->
						{
							int t = h.getTanks();
							for(int i = 0; i < t; i++)
							{
								var ft = h.getFluidInTank(i);
								if(is(ft))
									return true;
							}
							return false;
						})
						.orElse(false);
	}
	
	public boolean has(ItemStack stack, int minAmount)
	{
		return !stack.isEmpty() &&
				FluidUtil.getFluidHandler(stack)
						.resolve()
						.map(h ->
						{
							int amt = 0;
							int t = h.getTanks();
							for(int i = 0; i < t; i++)
							{
								var ft = h.getFluidInTank(i);
								if(is(ft))
								{
									amt += ft.getAmount();
									if(amt >= minAmount)
										return true;
								}
							}
							return amt >= minAmount;
						})
						.orElse(false);
	}
	
	@Override
	public void performRegister(RegisterEvent e, ResourceLocation fluidId)
	{
		var key = e.getRegistryKey();
		
		if(ForgeRegistries.Keys.FLUID_TYPES.equals(key))
		{
			if(type instanceof IRegisterListener rl) rl.onPreRegistered(fluidId);
			e.register(ForgeRegistries.Keys.FLUID_TYPES, fluidId, Cast.constant(type));
			if(type instanceof IRegisterListener rl) rl.onPostRegistered(fluidId);
		}
		
		if(ForgeRegistries.Keys.FLUIDS.equals(key))
		{
			if(getSource() instanceof IRegisterListener rl) rl.onPreRegistered(fluidId);
			if(getFlowing() instanceof IRegisterListener rl) rl.onPreRegistered(subId(fluidId, "flow"));
			
			e.register(ForgeRegistries.Keys.FLUIDS, fluidId, source::get);
			e.register(ForgeRegistries.Keys.FLUIDS, subId(fluidId, "flow"), flowing::get);
			
			if(getSource() instanceof IRegisterListener rl) rl.onPostRegistered(fluidId);
			if(getFlowing() instanceof IRegisterListener rl) rl.onPostRegistered(subId(fluidId, "flow"));
			
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ItemBlockRenderTypes.setRenderLayer(getSource(), Cast.get2(renderType)));
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ItemBlockRenderTypes.setRenderLayer(getFlowing(), Cast.get2(renderType)));
		}
		
		if(block != null && ForgeRegistries.Keys.BLOCKS.equals(key))
		{
			if(getBlock() instanceof IRegisterListener rl) rl.onPreRegistered(subId(fluidId, "bucket"));
			e.register(ForgeRegistries.Keys.BLOCKS, fluidId, block::get);
			if(getBlock() instanceof IRegisterListener rl) rl.onPostRegistered(subId(fluidId, "bucket"));
		}
		
		if(bucket != null && ForgeRegistries.Keys.ITEMS.equals(key))
		{
			if(getBucket() instanceof IRegisterListener rl) rl.onPreRegistered(subId(fluidId, "bucket"));
			e.register(ForgeRegistries.Keys.ITEMS, subId(fluidId, "bucket"), bucket);
			if(getBucket() instanceof IRegisterListener rl) rl.onPostRegistered(subId(fluidId, "bucket"));
		}
		
		for(var tag : fluidTags)
			TagAdapter.bind(tag, this.source.get(), this.flowing.get());
	}
	
	@Override
	public Item asItem()
	{
		return bucket.get();
	}
	
	public static Builder builder(Supplier<FluidType> typeGenerator)
	{
		return new Builder(typeGenerator);
	}
	
	public static Builder builder(FluidType.Properties typeProps)
	{
		return new Builder(() -> new FluidTypeHL(typeProps));
	}
	
	public static class Builder
	{
		protected final Supplier<FluidType> typeGenerator;
		protected final List<TagKey<Fluid>> fluidTags = Lists.newArrayList();
		protected final List<CreativeModeTab> tabs = Lists.newArrayList();
		protected UnaryOperator<ForgeFlowingFluid.Properties> propertyModifier = UnaryOperator.identity();
		protected Function2<Supplier<FlowingFluid>, List<CreativeModeTab>, Item> bucket;
		protected Function<Supplier<? extends FlowingFluid>, LiquidBlock> block;
		
		protected Function<ForgeFlowingFluid.Properties, ForgeFlowingFluid.Source> sourceGen = ForgeFlowingFluid.Source::new;
		protected Function<ForgeFlowingFluid.Properties, ForgeFlowingFluid.Flowing> flowingGen = ForgeFlowingFluid.Flowing::new;
		
		protected Supplier<Supplier<RenderType>> renderType = () -> RenderType::solid;
		
		public Builder(Supplier<FluidType> typeGenerator)
		{
			this.typeGenerator = typeGenerator;
		}
		
		public Builder withBucket()
		{
			return withBucket((fluid, tabs) -> new BucketItem(fluid,
					new Item.Properties()
							.craftRemainder(Items.BUCKET)
							.stacksTo(1)
			)
			{
				@Override
				public Collection<CreativeModeTab> getCreativeTabs()
				{
					return tabs;
				}
			});
		}
		
		public Builder withBlock()
		{
			return withBlock(flowing -> new LiquidBlock(flowing,
					BlockBehaviour.Properties.copy(Blocks.WATER)
							.noCollission()
							.strength(100.0F)
							.noLootTable()
			));
		}
		
		public Builder propertyModifier(UnaryOperator<ForgeFlowingFluid.Properties> propertyModifier)
		{
			var pm = this.propertyModifier;
			this.propertyModifier = v -> propertyModifier.apply(pm.apply(v));
			return this;
		}
		
		public Builder withBucket(Function2<Supplier<FlowingFluid>, List<CreativeModeTab>, Item> bucket)
		{
			this.bucket = bucket;
			return this;
		}
		
		public Builder withBlock(Function<Supplier<? extends FlowingFluid>, LiquidBlock> block)
		{
			this.block = block;
			return this;
		}
		
		public Builder withSource(Function<ForgeFlowingFluid.Properties, ForgeFlowingFluid.Source> sourceGen)
		{
			this.sourceGen = sourceGen;
			return this;
		}
		
		public Builder withFlowing(Function<ForgeFlowingFluid.Properties, ForgeFlowingFluid.Flowing> flowingGen)
		{
			this.flowingGen = flowingGen;
			return this;
		}
		
		public Builder withRenderType(Supplier<Supplier<RenderType>> renderType)
		{
			this.renderType = renderType;
			return this;
		}
		
		public Builder addFluidTag(TagKey<Fluid> tag)
		{
			fluidTags.add(tag);
			return this;
		}
		
		public Builder addFluidTags(List<TagKey<Fluid>> fluidTags)
		{
			this.fluidTags.addAll(fluidTags);
			return this;
		}
		
		public Builder addToTab(CreativeModeTab tab)
		{
			tabs.add(tab);
			return this;
		}
		
		public Builder addToTabs(Collection<CreativeModeTab> tab)
		{
			tabs.addAll(tab);
			return this;
		}
		
		public FluidFactory build()
		{
			return new FluidFactory(
					typeGenerator,
					bucket,
					propertyModifier,
					block,
					sourceGen,
					flowingGen
			).withRenderType(renderType)
					.addFluidTags(fluidTags)
					.addToTabs(tabs);
		}
	}
}