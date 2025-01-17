package org.zeith.hammerlib.util.mcf.fluid;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import org.zeith.hammerlib.api.fluid.IExtendedFluidType;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.CommonMessages;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Predicate;

public class FluidHelper
{
	public static Optional<TooltipComponent> getFluidTooltipComponent(FluidStack fluid, boolean showCapacity, int capacity, boolean advancedItemTooltips)
	{
		return IExtendedFluidType.of(fluid).getTooltipImage(fluid, showCapacity, capacity, advancedItemTooltips);
	}
	
	public static List<Component> getFluidTooltip(FluidStack fluid, boolean showCapacity, int capacity, boolean advancedItemTooltips)
	{
		List<Component> tooltip = new ArrayList<>();
		
		if(!fluid.isEmpty())
		{
			var dn = fluid.getHoverName();
			tooltip.add(Component.empty().append(dn).withStyle(fluid.getFluid().getFluidType().getRarity(fluid).getStyleModifier()));
			
			var nf = DecimalFormat.getIntegerInstance();
			
			tooltip.add((showCapacity
						 ? Component.translatable("info." + HLConstants.MOD_ID + ".fluid_capped", nf.format(fluid.getAmount()), nf.format(capacity))
						 : Component.translatable("info." + HLConstants.MOD_ID + ".fluid_uncapped", nf.format(fluid.getAmount()))
			).withStyle(ChatFormatting.GRAY));
			
			if(advancedItemTooltips)
				tooltip.add(Component.literal(BuiltInRegistries.FLUID.getKey(fluid.getFluid()).toString())
						.withStyle(ChatFormatting.DARK_GRAY));
		} else
			tooltip.add(CommonMessages.EMPTY.get());
		
		return tooltip;
	}
	
	public static FluidStack limit(FluidStack fluid, int max)
	{
		if(fluid.isEmpty() || fluid.getAmount() <= max) return fluid;
		return withAmount(fluid, max);
	}
	
	public static FluidStack withAmount(FluidStack fluid, int amount)
	{
		if(fluid.isEmpty()) return FluidStack.EMPTY;
		var fs = fluid.copy();
		fs.setAmount(amount);
		return fs;
	}
	
	public static boolean anyFluidMatches(ItemStack stack, Predicate<FluidStack> filter)
	{
		return Optional.ofNullable(stack.getCapability(Capabilities.FluidHandler.ITEM)).map(h ->
		{
			var slots = h.getTanks();
			for(int i = 0; i < slots; ++i)
				if(filter.test(h.getFluidInTank(i)))
					return true;
			return false;
		}).orElse(false);
	}
	
	public static boolean allFluidsMatch(ItemStack stack, Predicate<FluidStack> filter)
	{
		return Optional.ofNullable(stack.getCapability(Capabilities.FluidHandler.ITEM)).map(h ->
		{
			var slots = h.getTanks();
			for(int i = 0; i < slots; ++i)
				if(!filter.test(h.getFluidInTank(i)))
					return false;
			return true;
		}).orElse(false);
	}
	
	public static boolean noneFluidsMatch(ItemStack stack, Predicate<FluidStack> filter)
	{
		return Optional.ofNullable(stack.getCapability(Capabilities.FluidHandler.ITEM)).map(h ->
		{
			var slots = h.getTanks();
			for(int i = 0; i < slots; ++i)
				if(filter.test(h.getFluidInTank(i)))
					return false;
			return true;
		}).orElse(false);
	}
	
	public static boolean isFluidContainerEmpty(ItemStack stack)
	{
		return allFluidsMatch(stack, FluidStack::isEmpty);
	}
	
	public static boolean isFluidContainerFull(ItemStack stack)
	{
		return Optional.ofNullable(stack.getCapability(Capabilities.FluidHandler.ITEM)).map(h ->
		{
			FluidStack fs;
			var slots = h.getTanks();
			for(int i = 0; i < slots; ++i)
				if((fs = h.getFluidInTank(i)).isEmpty() || fs.getAmount() < h.getTankCapacity(i))
					return false;
			return true;
		}).orElse(false);
	}
}