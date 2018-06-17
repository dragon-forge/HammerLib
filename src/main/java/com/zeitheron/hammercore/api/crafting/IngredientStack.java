package com.zeitheron.hammercore.api.crafting;

public class IngredientStack<T extends IBaseIngredient>
{
	public T ingredient;
	public int amount;
	
	public IngredientStack(T ingredient, int amount)
	{
		this.ingredient = ingredient;
		this.amount = amount;
	}
}