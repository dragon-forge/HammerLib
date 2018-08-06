package com.zeitheron.hammercore.api.crafting;

/**
 * The stack implementation for {@link IBaseIngredient} as delegate.
 */
public class IngredientStack<T extends IBaseIngredient>
{
	/** The ingredient stored in the stack */
	public T ingredient;
	
	/** The amount of ingredient stored in the stack */
	public int amount;
	
	public IngredientStack(T ingredient, int amount)
	{
		this.ingredient = ingredient;
		this.amount = amount;
	}
}