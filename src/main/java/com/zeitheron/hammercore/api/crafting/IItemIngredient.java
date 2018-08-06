package com.zeitheron.hammercore.api.crafting;

import net.minecraft.inventory.IInventory;

/**
 * An item implementation of {@link IBaseIngredient} that allows for items to be
 * taken from {@link IInventory}
 */
public interface IItemIngredient<T extends IItemIngredient> extends IBaseIngredient
{
	/**
	 * Traces if it is possible to take ingredient's item content.
	 * 
	 * @param inv
	 *            the {@link IInventory} to be tested
	 * @param stack
	 *            the stack that will be attempted to extract
	 * @return if the content can be taken.
	 */
	boolean canTakeFrom(IInventory inv, IngredientStack<T> stack);
	
	/**
	 * Tries to extract the items from the inventory.
	 * 
	 * @param inv
	 *            the {@link IInventory} to be emptied
	 * @param stack
	 *            the stack that will be attempted to extract
	 * @return if the content was taken.
	 */
	boolean takeFrom(IInventory inv, IngredientStack<T> stack);
}