package com.zeitheron.hammercore.utils;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidHelper
{
	private static final FluidStack[] EMPTY = new FluidStack[0];
	
	public static boolean canAccept(IFluidHandler handler, FluidStack fluid)
	{
		return handler.fill(fluid, false) > 0;
	}
	
	public static boolean containsFluid(Fluid fluid, FluidStack[] stacks)
	{
		for(FluidStack stack : stacks)
			if(stack != null && stack.getFluid() == fluid)
				return true;
		return false;
	}
	
	public static int getFluidAmount(Fluid fluid, FluidStack[] stacks)
	{
		int quant = 0;
		for(FluidStack stack : stacks)
			if(stack != null && stack.getFluid() == fluid)
				quant += stack.amount;
		return quant;
	}
	
	public static boolean drainFluid(Fluid fluid, int amount, ItemStack stack)
	{
		if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
		{
			IFluidHandlerItem ihfi = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if(ihfi != null)
			{
				FluidStack f = ihfi.drain(new FluidStack(fluid, amount), false);
				if(f != null && f.amount == amount)
				{
					ihfi.drain(new FluidStack(fluid, amount), true);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static int drainAvaliable(Fluid fluid, int amount, ItemStack stack)
	{
		int quant = 0;
		
		if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
		{
			IFluidHandlerItem ihfi = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if(ihfi != null)
			{
				FluidStack f = ihfi.drain(new FluidStack(fluid, amount), true);
				if(f != null)
					quant += f.amount;
			}
		}
		
		return quant;
	}
	
	public static FluidStack[] getFluids(ItemStack stack)
	{
		if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
		{
			IFluidHandlerItem ihfi = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if(ihfi != null)
			{
				IFluidTankProperties[] props = ihfi.getTankProperties();
				FluidStack[] stacks = new FluidStack[props.length];
				for(int i = 0; i < props.length; ++i)
					stacks[i] = props[i].getContents();
				return stacks;
			}
		}
		
		return EMPTY;
	}
}