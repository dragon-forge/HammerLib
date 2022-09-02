package org.zeith.hammerlib.api.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * An item implementation of {@link IBaseIngredient} that allows for items to be
 * taken from {@link Container}
 */
public interface IItemIngredient<T extends IItemIngredient<T>>
		extends IBaseIngredient
{
	/**
	 * Traces if it is possible to take ingredient's item content.
	 *
	 * @param inv   the {@link Container} to be tested
	 * @param stack the stack that will be attempted to extract
	 * @return if the content can be taken.
	 */
	boolean canTakeFrom(Container inv, IngredientStack<T> stack);

	/**
	 * Tries to extract the items from the inventory.
	 *
	 * @param inv   the {@link Container} to be emptied
	 * @param stack the stack that will be attempted to extract
	 * @return if the content was taken.
	 */
	boolean takeFrom(Container inv, IngredientStack<T> stack);

	/**
	 * Transforms this ingredient to vanilla item ingredient for easier management.
	 */
	Ingredient asIngredient();

	/**
	 * The amount of this item required for the recipe.
	 * By default, is 1, but may be multiplied with {@link #quantify(int)} method.
	 */
	default int getCount()
	{
		return 1;
	}

	IQuantifiableIngredient<?> quantify(int count);

	interface IQuantifiableIngredient<T extends IQuantifiableIngredient<T>>
			extends IItemIngredient<T>
	{
		int getCount();
	}
}