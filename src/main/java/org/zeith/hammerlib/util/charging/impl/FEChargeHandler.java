package org.zeith.hammerlib.util.charging.impl;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.zeith.hammerlib.util.charging.IChargeHandler;
import org.zeith.hammerlib.util.charging.fe.FECharge;

import java.util.concurrent.atomic.AtomicReference;

@IChargeHandler.ChargeHandler(FECharge.class)
public class FEChargeHandler
		implements IChargeHandler<FECharge>
{
	@Override
	public String getId()
	{
		return "FE";
	}
	
	@Override
	public boolean canCharge(ItemStack stack, FECharge charge)
	{
		return stack.getCapability(ForgeCapabilities.ENERGY, null).map(c -> c.receiveEnergy(charge.FE, true) > 0).orElse(false);
	}
	
	@Override
	public FECharge charge(AtomicReference<ItemStack> stack, FECharge charge, ChargeAction action)
	{
		return stack.get().getCapability(ForgeCapabilities.ENERGY, null)
				.map(cap -> charge.discharge(cap.receiveEnergy(charge.FE, action.simulate())))
				.orElse(charge);
	}
}