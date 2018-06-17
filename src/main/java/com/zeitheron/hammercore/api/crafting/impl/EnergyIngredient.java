package com.zeitheron.hammercore.api.crafting.impl;

import com.zeitheron.hammercore.api.EnergyUnit;
import com.zeitheron.hammercore.api.crafting.INumericalIngredient;

public class EnergyIngredient implements INumericalIngredient<Double>
{
	public double x;
	public EnergyUnit unit;
	
	public EnergyIngredient(double x, EnergyUnit unit)
	{
		this.x = x;
		this.unit = unit;
	}
	
	@Override
	public Double getAmount()
	{
		return x;
	}
	
	@Override
	public double toRFRatio()
	{
		return unit.toRF;
	}
}