package org.zeith.hammerlib.core.adapter;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Map;

public class RecipeManagerAdapter
{
	public static Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> getRecipeMap(RecipeManager manager)
	{
		return manager.recipes;
	}

	public static void setRecipeMap(RecipeManager manager, Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipeMap)
	{
		manager.recipes = recipeMap;
	}
}