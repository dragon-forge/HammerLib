package org.zeith.hammerlib.util.charging.impl;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
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
		return stack.getCapability(CapabilityEnergy.ENERGY, null).map(c -> c.receiveEnergy(charge.FE, true) > 0).orElse(false);
	}

	@Override
	public FECharge charge(ItemStack stack, FECharge charge, ChargeAction simulate)
	{
		return stack.getCapability(CapabilityEnergy.ENERGY, null)
				.map(cap -> charge.discharge(cap.receiveEnergy(charge.FE, simulate.simulate())))
				.orElse(charge);
	}
}