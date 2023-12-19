package org.zeith.hammerlib.util.charging.impl;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.zeith.hammerlib.util.charging.IChargeHandler;
import org.zeith.hammerlib.util.charging.fluid.FluidCharge;

import java.util.concurrent.atomic.AtomicReference;

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
		return stack.getCapability(Capabilities.FLUID_HANDLER_ITEM, null)
				.map(cap -> cap.fill(charge.fluid.copy(), IFluidHandler.FluidAction.SIMULATE) > 0)
				.orElse(false);
	}
	
	@Override
	public FluidCharge charge(AtomicReference<ItemStack> stack, FluidCharge charge, ChargeAction action)
	{
		return stack.get().getCapability(Capabilities.FLUID_HANDLER_ITEM, null)
				.map(cap ->
				{
					var c = charge.discharge(cap.fill(charge.fluid.copy(), action.asFluidAction()));
					stack.set(cap.getContainer());
					return c;
				})
				.orElse(charge);
	}
}