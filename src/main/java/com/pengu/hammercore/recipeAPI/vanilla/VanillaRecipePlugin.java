package com.pengu.hammercore.recipeAPI.vanilla;

import com.pengu.hammercore.recipeAPI.RecipePlugin;
import com.pengu.hammercore.recipeAPI.iRecipePlugin;
import com.pengu.hammercore.recipeAPI.iRecipeTypeRegistry;
import com.pengu.hammercore.recipeAPI.vanilla.brewing.BrewingRecipeType;
import com.pengu.hammercore.recipeAPI.vanilla.furnace.SmeltingRecipeType;

@RecipePlugin
public class VanillaRecipePlugin implements iRecipePlugin
{
	@Override
	public void registerTypes(iRecipeTypeRegistry reg)
	{
		reg.register(new SmeltingRecipeType());
		reg.register(new BrewingRecipeType());
	}
}