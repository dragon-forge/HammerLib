package org.zeith.hammerlib.util.charging.impl;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
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
		return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)
				.map(cap -> cap.fill(charge.fluid.copy(), IFluidHandler.FluidAction.SIMULATE) > 0)
				.orElse(false);
	}

	@Override
	public FluidCharge charge(ItemStack stack, FluidCharge charge, ChargeAction simulate)
	{
		return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)
				.map(cap -> charge.discharge(cap.fill(charge.fluid.copy(), simulate.asFluidAction())))
				.orElse(charge);
	}
}