package org.zeith.hammerlib.api;

import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

public interface IRecipeProvider
{
	void provideRecipes(RegisterRecipesEvent event);
}