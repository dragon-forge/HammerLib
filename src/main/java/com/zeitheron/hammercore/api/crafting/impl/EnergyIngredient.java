package com.zeitheron.hammercore.api.crafting.impl;

import com.zeitheron.hammercore.api.EnergyUnit;
import com.zeitheron.hammercore.api.crafting.INumericalIngredient;

/**
 * An energy implementation of {@link INumericalIngredient} that involves FE in
 * recipes.
 */
public class EnergyIngredient implements INumericalIngredient<Double>
{
	public double x;
	public EnergyUnit unit;
	
	/**
	 * Creates a new energy ingredient.
	 * 
	 * @param x
	 *            the amount of energy that this ingredient holds
	 * @param unit
	 *            the unit of energy the "x" is passed
	 */
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