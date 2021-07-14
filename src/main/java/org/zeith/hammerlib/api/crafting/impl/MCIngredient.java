package org.zeith.hammerlib.api.crafting.impl;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.Ingredient;
import org.zeith.hammerlib.api.crafting.IItemIngredient;
import org.zeith.hammerlib.api.crafting.IngredientStack;
import org.zeith.hammerlib.api.items.ConsumableItem;

/**
 * The {@link Ingredient} implementation of {@link IItemIngredient}
 */
public class MCIngredient
		implements IItemIngredient<MCIngredient>
{
	public final Ingredient ingredient;

	public MCIngredient(Ingredient ingredient)
	{
		this.ingredient = ingredient;
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

	@Override
	public Ingredient asIngredient()
	{
		return ingredient;
	}

	@Override
	public IQuantifiableIngredient<?> quantify(int count)
	{
		return new MCQIngredient(ingredient, count);
	}

	public static class MCQIngredient
			implements IQuantifiableIngredient<MCQIngredient>
	{
		public final Ingredient ingredient;
		public final int count;

		public MCQIngredient(Ingredient ingredient, int count)
		{
			this.ingredient = ingredient;
			this.count = count;
		}

		@Override
		public boolean canTakeFrom(IInventory inv, IngredientStack<MCQIngredient> stack)
		{
			ConsumableItem CI = new ConsumableItem(stack.amount, stack.ingredient.ingredient);
			return CI.canConsume(inv);
		}

		@Override
		public boolean takeFrom(IInventory inv, IngredientStack<MCQIngredient> stack)
		{
			ConsumableItem CI = new ConsumableItem(stack.amount, stack.ingredient.ingredient);
			return CI.consume(inv);
		}

		@Override
		public Ingredient asIngredient()
		{
			return ingredient;
		}

		@Override
		public IQuantifiableIngredient<?> quantify(int count)
		{
			return new MCQIngredient(ingredient, this.count * count);
		}

		@Override
		public int getCount()
		{
			return count;
		}
	}
}