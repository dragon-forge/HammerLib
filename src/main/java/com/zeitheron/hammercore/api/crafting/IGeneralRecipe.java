package com.zeitheron.hammercore.api.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface IGeneralRecipe
{
	NonNullList<IBaseIngredient> getIngredients();
	
	default ItemStack getRecipeOutput()
	{
		return getRecipeOutputOriginal().copy();
	}
	
	ItemStack getRecipeOutputOriginal();
}