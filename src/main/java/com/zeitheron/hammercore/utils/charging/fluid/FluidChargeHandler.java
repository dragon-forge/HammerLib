package com.zeitheron.hammercore.utils.charging.fluid;

import com.zeitheron.hammercore.utils.charging.IChargeHandler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class FluidChargeHandler implements IChargeHandler<FluidCharge>
{
	@Override
	public String getId()
	{
		return "Fluid";
	}
	
	@Override
	public boolean canCharge(ItemStack stack, FluidCharge charge)
	{
		IFluidHandlerItem cap = stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) ? stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) : null;
		return cap != null && cap.fill(charge.fluid.copy(), false) > 0;
	}
	
	@Override
	public FluidCharge charge(ItemStack stack, FluidCharge charge, boolean simulate)
	{
		IFluidHandlerItem cap = stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) ? stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) : null;
		return cap != null ? charge.discharge(cap.fill(charge.fluid.copy(), !simulate)) : charge;
	}
}