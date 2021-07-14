package org.zeith.hammerlib.util.charging.impl;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.zeith.hammerlib.util.charging.IChargeHandler;
import org.zeith.hammerlib.util.charging.fluid.FluidCharge;

@IChargeHandler.ChargeHandler(FluidCharge.class)
public class FluidChargeHandler
		implements IChargeHandler<FluidCharge>
{
	@Override
	public String getId()
	{
		return "Fluid";
	}

	@Override
	public boolean canCharge(ItemStack stack, FluidCharge charge)
	{
		IFluidHandlerItem cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).orElse(null);
		return cap != null && cap.fill(charge.fluid.copy(), IFluidHandler.FluidAction.SIMULATE) > 0;
	}

	@Override
	public FluidCharge charge(ItemStack stack, FluidCharge charge, ChargeAction simulate)
	{
		IFluidHandlerItem cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).orElse(null);
		return cap != null ? charge.discharge(cap.fill(charge.fluid.copy(), simulate.asFluidAction())) : charge;
	}
}