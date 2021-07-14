package org.zeith.hammerlib.api.crafting.impl;

import org.zeith.hammerlib.api.crafting.IEnergyIngredient;
import org.zeith.hammerlib.api.energy.EnergyUnit;

/**
 * An energy implementation of {@link IEnergyIngredient} that involves FE in
 * recipes.
 */
public class EnergyIngredient
		implements IEnergyIngredient<Double>
{
	public double x;
	public EnergyUnit unit;

	/**
	 * Creates a new energy ingredient.
	 *
	 * @param x    the amount of energy that this ingredient holds
	 * @param unit the unit of energy the "x" is passed
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
	public EnergyUnit getUnit()
	{
		return unit;
	}
}