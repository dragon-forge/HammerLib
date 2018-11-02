package com.zeitheron.hammercore.api.crafting.impl;

import com.zeitheron.hammercore.api.crafting.ICustomIngredient;
import com.zeitheron.hammercore.api.crafting.IItemIngredient;
import com.zeitheron.hammercore.api.crafting.IngredientStack;
import com.zeitheron.hammercore.utils.ConsumableItem;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.Ingredient;

/**
 * The {@link Ingredient} implementation of {@link IItemIngredient}
 */
public class MCIngredient implements ICustomIngredient<Ingredient>, IItemIngredient<MCIngredient>
{
	public final Ingredient ingredient;
	
	public MCIngredient(Ingredient ingredient)
	{
		this.ingredient = ingredient;
	}
	
	@Override
	public Ingredient getCopy()
	{
		return Ingredient.fromStacks(getOrigin().getMatchingStacks());
	}
	
	@Override
	public Ingredient getOrigin()
	{
		return ingredient;
	}
	
	@Override
	public boolean canTakeFrom(IInventory inv, IngredientStack<MCIngredient> stack)
	{
		ConsumableItem CI = new ConsumableItem(stack.amount, stack.ingredient.ingredient);
		return CI.canConsume(inv);
	}
	
	@Override
	public boolean takeFrom(IInventory inv, IngredientStack<MCIngredient> stack)
	{
		ConsumableItem CI = new ConsumableItem(stack.amount, stack.ingredient.ingredient);
		return CI.consume(inv);
	}
}