package com.pengu.hammercore.recipeAPI.vanilla.brewing;

import java.util.ArrayList;
import java.util.List;

import com.pengu.hammercore.recipeAPI.BrewingRecipe.BSR;

import net.minecraft.item.ItemStack;

public class BrewingRecipeHC
{
	public BSR recipeInstance;
	public ItemStack input, output;
	public final List<ItemStack> ingredients = new ArrayList<>();
}