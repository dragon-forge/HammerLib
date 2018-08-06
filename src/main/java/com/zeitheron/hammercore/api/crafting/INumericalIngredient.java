package com.zeitheron.hammercore.api.crafting;

import com.zeitheron.hammercore.api.crafting.impl.EnergyIngredient;

/**
 * A number tree ingredient implementation of {@link IBaseIngredient}. Allows
 * for numerical values such as FE (see {@link EnergyIngredient})
 */
public interface INumericalIngredient<T extends Number> extends IBaseIngredient
{
	/**
	 * Gets the value of the ingredient.
	 */
	T getAmount();
	
	/**
	 * Gets the additional info about this ingredient. May be used if any other
	 * data should be stored besides a number.
	 */
	default Object getMeta()
	{
		return null;
	}
	
	default double getStackCost(IngredientStack<? extends INumericalIngredient> stack)
	{
		return stack.ingredient.getRFCost() * stack.amount;
	}
	
	/**
	 * Gets the cost of this ingredient in Redstone Flux (RF).
	 */
	default double getRFCost()
	{
		if(getAmount() == null)
			return 0;
		return toRFRatio() * getAmount().doubleValue();
	}
	
	/**
	 * Gets the conversion ratio of 1 unit of this ingredient to RF. For EU this
	 * value would be 4, since 4 RF convert to 1 EU.
	 */
	default double toRFRatio()
	{
		return 1;
	}
}