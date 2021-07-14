package org.zeith.hammerlib.api.crafting;

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
}