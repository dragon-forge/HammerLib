package com.zeitheron.hammercore.api.crafting.impl;

import com.zeitheron.hammercore.api.crafting.ICraftingResult;
import com.zeitheron.hammercore.utils.charging.fe.FECharge;

public class ForgeEnergyResult implements ICraftingResult<FECharge>
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