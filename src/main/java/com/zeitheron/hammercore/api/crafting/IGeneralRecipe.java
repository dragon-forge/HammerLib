package com.zeitheron.hammercore.api.crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * A base interface for generic recipes. This may be used for registries such as
 * grinding or others, that have single output and you don't want to make a lot
 * of code for them. See {@link AbstractRecipeRegistry}.
 */
public interface IGeneralRecipe
{
	/**
	 * @return all inputs that are required for the recipe.
	 */
	NonNullList<IBaseIngredient> getIngredients();
	
	/**
	 * Gives original instance of the item that may be tweaked.
	 * To set a new item, use {@link com.zeitheron.hammercore.utils.ItemStackUtil#setItem(ItemStack, Item)}, other properties are tweakable directly.
	 *
	 * @return the original result item for this recipe.
	 */
	ItemStack getRecipeOutputOriginal();
	
	/**
	 * @return a result item for this recipe.
	 */
	default ItemStack getRecipeOutput()
	{
		return getRecipeOutputOriginal().copy();
	}
}