package com.zeitheron.hammercore.api.crafting;

import java.util.ArrayList;
import java.util.List;

/**
 * A general registry for any subtype of {@link IGeneralRecipe}
 */
public class GeneralRecipeRegistry<T extends IGeneralRecipe>
{
	public static final GeneralRecipeRegistry<?> INSTANCE = new GeneralRecipeRegistry<>();
	
	/**
	 * A list of all registries known to Hammer Core.
	 */
	public static final List<GeneralRecipeRegistry> REGISTRIES = new ArrayList<>();
	{
		REGISTRIES.add(this);
	}
	
	/**
	 * A list that contains all recipes within this registry
	 */
	public final List<IGeneralRecipe> recipes = new ArrayList<>();
	
	/**
	 * Adds a recipe to this registry.
	 */
	public void addRecipe(IGeneralRecipe rec)
	{
		if(!recipes.contains(rec))
			recipes.add(rec);
	}
	
	/**
	 * Removes a recipe to this registry.
	 */
	public void removeRecipe(IGeneralRecipe rec)
	{
		if(recipes.contains(rec))
			recipes.remove(rec);
	}
}