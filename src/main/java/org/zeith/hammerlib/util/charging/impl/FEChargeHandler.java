package org.zeith.hammerlib.util.charging.impl;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
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
		var c = stack.getCapability(Capabilities.EnergyStorage.ITEM);
		if(c == null) return false;
		return c.receiveEnergy(charge.FE, true) > 0;
	}
	
	@Override
	public FECharge charge(AtomicReference<ItemStack> stack, FECharge charge, ChargeAction action)
	{
		var cap = stack.get().getCapability(Capabilities.EnergyStorage.ITEM);
		if(cap == null) return charge;
		return charge.discharge(cap.receiveEnergy(charge.FE, action.simulate()));
	}
}