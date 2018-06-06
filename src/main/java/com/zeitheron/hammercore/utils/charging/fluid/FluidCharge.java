package com.zeitheron.hammercore.utils.charging.fluid;

import com.zeitheron.hammercore.utils.charging.AbstractCharge;

import net.minecraftforge.fluids.FluidStack;

public class FluidCharge extends AbstractCharge
{
	public FluidStack fluid;
	
	public FluidCharge(FluidStack fluid)
	{
		this.fluid = fluid;
	}
	
	public FluidCharge discharge(int amt)
	{
		FluidCharge fc = copy();
		if(fc.fluid != null)
			fc.fluid.amount -= amt;
		return fc;
	}
	
	@Override
	public boolean containsCharge()
	{
		return fluid != null && fluid.amount > 0;
	}
	
	@Override
	public FluidCharge copy()
	{
		return new FluidCharge(fluid == null ? null : fluid.copy());
	}
}