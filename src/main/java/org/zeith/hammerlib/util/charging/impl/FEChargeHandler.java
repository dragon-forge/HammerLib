package org.zeith.hammerlib.util.charging.impl;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.zeith.hammerlib.util.charging.IChargeHandler;
import org.zeith.hammerlib.util.charging.fe.FECharge;

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
		IEnergyStorage cap = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
		return cap != null && cap.receiveEnergy(charge.FE, true) > 0;
	}

	@Override
	public FECharge charge(ItemStack stack, FECharge charge, ChargeAction simulate)
	{
		IEnergyStorage cap = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
		return cap != null ? charge.discharge(cap.receiveEnergy(charge.FE, simulate.simulate())) : charge;
	}
}