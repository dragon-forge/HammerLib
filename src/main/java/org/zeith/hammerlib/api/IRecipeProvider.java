package org.zeith.hammerlib.api;

import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;
import org.zeith.hammerlib.event.recipe.SpoofRecipesEvent;

public interface IRecipeProvider
{
	void provideRecipes(RegisterRecipesEvent event);
	
	default void spoofRecipes(SpoofRecipesEvent event)
	{
	}
}