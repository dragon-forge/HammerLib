package org.zeith.hammerlib.api.crafting.impl;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import org.zeith.hammerlib.api.crafting.IItemIngredient;
import org.zeith.hammerlib.api.crafting.IngredientStack;
import org.zeith.hammerlib.api.items.ConsumableItem;
import org.zeith.hammerlib.util.java.ResettableLazy;

import java.util.function.Supplier;

/**
 * The {@link Ingredient} implementation of {@link IItemIngredient}
 */
public class MCIngredient
		implements IItemIngredient<MCIngredient>
{
	public final ResettableLazy<Ingredient> ingredient;

	public MCIngredient(Ingredient ingredient)
	{
		this.ingredient = ResettableLazy.of(ingredient);
	}

	public MCIngredient(ResettableLazy<Ingredient> ingredient)
	{
		this.ingredient = ingredient;
	}

	public MCIngredient(Supplier<Ingredient> ingredient)
	{
		this.ingredient = ResettableLazy.of(ingredient);
	}

	@Override
	public boolean canTakeFrom(Container inv, IngredientStack<MCIngredient> stack)
	{
		ConsumableItem CI = new ConsumableItem(stack.amount, stack.ingredient.ingredient.get());
		return CI.canConsume(inv);
	}

	@Override
	public boolean takeFrom(Container inv, IngredientStack<MCIngredient> stack)
	{
		ConsumableItem CI = new ConsumableItem(stack.amount, stack.ingredient.ingredient.get());
		return CI.consume(inv);
	}

	@Override
	public Ingredient asIngredient()
	{
		return ingredient.get();
	}

	@Override
	public IQuantifiableIngredient<?> quantify(int count)
	{
		return new MCQIngredient(ingredient, count);
	}

	@Override
	public void resetCache()
	{
		ingredient.reset();
	}

	public static class MCQIngredient
			implements IQuantifiableIngredient<MCQIngredient>
	{
		public final ResettableLazy<Ingredient> ingredient;
		public final int count;

		public MCQIngredient(Ingredient ingredient, int count)
		{
			this.ingredient = ResettableLazy.of(ingredient);
			this.count = count;
		}

		public MCQIngredient(ResettableLazy<Ingredient> ingredient, int count)
		{
			this.ingredient = ingredient;
			this.count = count;
		}

		public MCQIngredient(Supplier<Ingredient> ingredient, int count)
		{
			this.ingredient = ResettableLazy.of(ingredient);
			this.count = count;
		}

		@Override
		public boolean canTakeFrom(Container inv, IngredientStack<MCQIngredient> stack)
		{
			ConsumableItem CI = new ConsumableItem(stack.amount, stack.ingredient.ingredient.get());
			return CI.canConsume(inv);
		}

		@Override
		public boolean takeFrom(Container inv, IngredientStack<MCQIngredient> stack)
		{
			ConsumableItem CI = new ConsumableItem(stack.amount, stack.ingredient.ingredient.get());
			return CI.consume(inv);
		}

		@Override
		public Ingredient asIngredient()
		{
			return ingredient.get();
		}

		@Override
		public void resetCache()
		{
			ingredient.reset();
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