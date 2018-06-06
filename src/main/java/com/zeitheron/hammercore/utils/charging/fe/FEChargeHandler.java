package com.zeitheron.hammercore.utils.charging.fe;

import com.zeitheron.hammercore.utils.charging.IChargeHandler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class FEChargeHandler implements IChargeHandler<FECharge>
{
	@Override
	public String getId()
	{
		return "FE";
	}
	
	@Override
	public boolean canCharge(ItemStack stack, FECharge charge)
	{
		IEnergyStorage cap = stack.hasCapability(CapabilityEnergy.ENERGY, null) ? stack.getCapability(CapabilityEnergy.ENERGY, null) : null;
		return cap != null && cap.receiveEnergy(charge.FE, true) > 0;
	}
	
	@Override
	public FECharge charge(ItemStack stack, FECharge charge, boolean simulate)
	{
		IEnergyStorage cap = stack.hasCapability(CapabilityEnergy.ENERGY, null) ? stack.getCapability(CapabilityEnergy.ENERGY, null) : null;
		return cap != null ? charge.discharge(cap.receiveEnergy(charge.FE, simulate)) : charge;
	}
}