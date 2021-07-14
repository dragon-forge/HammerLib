package org.zeith.hammerlib.api.crafting;

import org.zeith.hammerlib.api.crafting.impl.EnergyIngredient;
import org.zeith.hammerlib.api.energy.EnergyUnit;

/**
 * A number tree ingredient implementation of {@link IBaseIngredient}. Allows
 * for numerical values such as FE (see {@link EnergyIngredient})
 */
public interface IEnergyIngredient<T extends Number>
		extends IBaseIngredient
{
	/**
	 * @return the value of the ingredient.
	 */
	T getAmount();

	/**
	 * @return the additional info about this ingredient. May be used if any
	 * other data should be stored besides a number.
	 */
	default Object getMeta()
	{
		return null;
	}

	default double getStackCost(IngredientStack<? extends IEnergyIngredient<?>> stack)
	{
		return stack.ingredient.getRFCost() * stack.amount;
	}

	/**
	 * @return the cost of this ingredient in Redstone Flux (RF).
	 */
	default double getRFCost()
	{
		if(getAmount() == null) return 0;
		return EnergyUnit.FE.convertFrom(getAmount().doubleValue(), getUnit());
	}

	/**
	 * @return the conversion ratio of 1 unit of this ingredient to RF. For EU
	 * this value would be 4, since 4 RF convert to 1 EU.
	 */
	default EnergyUnit getUnit()
	{
		return EnergyUnit.FE;
	}
}