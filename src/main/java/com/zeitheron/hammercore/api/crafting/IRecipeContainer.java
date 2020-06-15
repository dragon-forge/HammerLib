package com.zeitheron.hammercore.api.crafting;

import java.util.Collection;

public interface IRecipeContainer<T extends IGeneralRecipe>
{
	Collection<T> getRecipes();
	
	Class<T> getType();
}