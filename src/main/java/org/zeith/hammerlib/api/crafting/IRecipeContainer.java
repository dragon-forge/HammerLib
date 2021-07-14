package org.zeith.hammerlib.api.crafting;

import java.util.Collection;

public interface IRecipeContainer<T extends IGeneralRecipe>
{
	Collection<T> getRecipes();

	Class<T> getType();

	default int getRecipeCount()
	{
		return getRecipes().size();
	}
}