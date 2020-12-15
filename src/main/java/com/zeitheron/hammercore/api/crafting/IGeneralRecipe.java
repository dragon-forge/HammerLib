package com.zeitheron.hammercore.api.crafting;

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
	 * @return the original result item for this recipe.
	 */
	ICraftingResult<?> getResult();
	
	/**
	 * @return a result as item for this recipe.
	 */
	@Deprecated
	default ItemStack getRecipeOutput()
	{
		ICraftingResult<?> result = getResult();
		if(ItemStack.class.isAssignableFrom(result.getType()))
			return ItemStack.class.cast(result.getBaseOutput());
		return ItemStack.EMPTY;
	}
}