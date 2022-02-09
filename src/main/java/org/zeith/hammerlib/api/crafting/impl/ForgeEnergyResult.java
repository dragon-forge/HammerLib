package org.zeith.hammerlib.api.crafting.impl;

import org.zeith.hammerlib.api.crafting.ICraftingResult;
import org.zeith.hammerlib.util.charging.fe.FECharge;

public class ForgeEnergyResult
		implements ICraftingResult<FECharge>
{
	protected FECharge output;

	public ForgeEnergyResult(FECharge output)
	{
		this.output = output;
	}

	@Override
	public FECharge getBaseOutput()
	{
		return output.copy();
	}

	@Override
	public Class<FECharge> getType()
	{
		return FECharge.class;
	}
}