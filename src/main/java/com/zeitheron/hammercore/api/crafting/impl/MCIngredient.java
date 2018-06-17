package com.zeitheron.hammercore.api.crafting.impl;

import com.pengu.hammercore.utils.ConsumableItem;
import com.zeitheron.hammercore.api.crafting.ICustomIngredient;
import com.zeitheron.hammercore.api.crafting.IItemIngredient;
import com.zeitheron.hammercore.api.crafting.IngredientStack;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.Ingredient;

public class MCIngredient implements ICustomIngredient<Ingredient>, IItemIngredient<MCIngredient>
{
	public final Ingredient ingredient;
	
	public MCIngredient(Ingredient ingredient)
	{
		this.ingredient = ingredient;
	}
	
	public Ingredient copy()
	{
		return Ingredient.fromStacks(getOrigin().getMatchingStacks());
	}
	
	@Override
	public Ingredient getCopy()
	{
		return copy();
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