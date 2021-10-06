package org.zeith.hammerlib.core.adapter;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class RecipeManagerAdapter
{
	public static Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> getRecipeMap(RecipeManager manager)
	{
		return manager.recipes;
	}

	public static void setRecipeMap(RecipeManager manager, Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipeMap)
	{
		manager.recipes = recipeMap;
	}
}