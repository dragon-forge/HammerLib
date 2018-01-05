package com.pengu.hammercore.common.match.fluid;

public class FluidMatchParams
{
	public boolean useNBT = false, useFluiddict = false, useCount = false, useMod = false;
	
	public FluidMatchParams setUseNBT(boolean nbt)
	{
		useNBT = nbt;
		return this;
	}
	
	public FluidMatchParams setUseFluiddict(boolean od)
	{
		useFluiddict = od;
		return this;
	}
	
	public FluidMatchParams setUseCount(boolean ctn)
	{
		useCount = ctn;
		return this;
	}
	
	public FluidMatchParams setUseMod(boolean mod)
	{
		useMod = mod;
		return this;
	}
}