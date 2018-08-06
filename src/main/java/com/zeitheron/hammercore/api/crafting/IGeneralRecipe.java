package com.zeitheron.hammercore.api.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * A base interface for generic recipes. This may be used for registries such as
 * grinding or others, that have single output and you don't want to make a lot
 * of code for them. See {@link GeneralRecipeRegistry}.
 */
public interface IGeneralRecipe
{
	/**
	 * @return all inputs that are required for the recipe.
	 */
	NonNullList<IBaseIngredient> getIngredients();
	
	/**
	 * @return a result item for this recipe.
	 */
	default ItemStack getRecipeOutput()
	{
		return getRecipeOutputOriginal().copy();
	}
	
	/**
	 * @return the original result item for this recipe.
	 */
	ItemStack getRecipeOutputOriginal();
}