package com.zeitheron.hammercore.api.crafting;

import java.util.ArrayList;
import java.util.List;

public class GeneralRecipeRegistry<T extends IGeneralRecipe>
{
	public static final GeneralRecipeRegistry<?> INSTANCE = new GeneralRecipeRegistry<>();
	
	/**
	 * Used to get all registries
	 */
	public static final List<GeneralRecipeRegistry> REGISTRIES = new ArrayList<>();
	{
		REGISTRIES.add(this);
	}
	
	public final List<IGeneralRecipe> recipes = new ArrayList<>();
	
	public void addRecipe(IGeneralRecipe rec)
	{
		if(!recipes.contains(rec))
			recipes.add(rec);
	}
	
	public void removeRecipe(IGeneralRecipe rec)
	{
		if(recipes.contains(rec))
			recipes.remove(rec);
	}
}