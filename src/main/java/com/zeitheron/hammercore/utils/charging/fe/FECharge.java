package com.zeitheron.hammercore.utils.charging.fe;

import com.zeitheron.hammercore.utils.charging.AbstractCharge;

public class FECharge extends AbstractCharge
{
	public int FE;
	
	public FECharge(int fe)
	{
		this.FE = Math.max(fe, 0);
	}
	
	public FECharge discharge(int fe)
	{
		return new FECharge(this.FE - fe);
	}
	
	@Override
	public boolean containsCharge()
	{
		return FE > 0;
	}
	
	@Override
	public FECharge copy()
	{
		return new FECharge(FE);
	}
}