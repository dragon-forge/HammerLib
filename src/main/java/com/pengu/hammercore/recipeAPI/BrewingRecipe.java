package com.pengu.hammercore.recipeAPI;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public enum BrewingRecipe implements IBrewingRecipe
{
	INSTANCE;
	
	public final Set<BSR> recipes = new HashSet<>();
	
	public BSR addRecipe(Ingredient input, Ingredient ingredient, ItemStack output)
	{
		BSR r = new BSR();
		r.input = input;
		r.ingredient = ingredient;
		r.output = output;
		recipes.add(r);
		return r;
	}
	
	@Override
	public boolean isInput(ItemStack input)
	{
		for(BSR r : recipes)
			if(r.input.apply(input))
				return true;
		return false;
	}
	
	@Override
	public boolean isIngredient(ItemStack ingredient)
	{
		for(BSR r : recipes)
			if(r.ingredient.apply(ingredient))
				return true;
		return false;
	}
	
	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient)
	{
		for(BSR r : recipes)
			if(r.input.apply(input) && r.ingredient.apply(ingredient))
				return r.output.copy();
		return ItemStack.EMPTY;
	}
	
	public static final class BSR
	{
		public Ingredient input, ingredient;
		public ItemStack output;
	}
}