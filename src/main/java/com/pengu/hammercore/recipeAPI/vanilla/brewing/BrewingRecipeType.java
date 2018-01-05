package com.pengu.hammercore.recipeAPI.vanilla.brewing;

import java.util.Arrays;

import com.pengu.hammercore.common.match.item.ItemListContainerHelper;
import com.pengu.hammercore.common.match.item.ItemMatchParams;
import com.pengu.hammercore.recipeAPI.BrewingRecipe;
import com.pengu.hammercore.recipeAPI.iRecipeType;

import mezz.jei.plugins.vanilla.brewing.BrewingRecipeWrapper;
import net.minecraft.nbt.NBTTagCompound;

public class BrewingRecipeType implements iRecipeType<BrewingRecipeHC>
{
	@Override
	public boolean isJeiSupported(BrewingRecipeHC recipe)
	{
		return true;
	}
	
	@Override
	public Object getJeiRecipeFor(BrewingRecipeHC recipe, boolean remove)
	{
		return new BrewingRecipeWrapper(recipe.ingredients, recipe.input, recipe.output);
	}
	
	@Override
	public String getTypeId()
	{
		return "brewing";
	}
	
	@Override
	public BrewingRecipeHC createRecipe(NBTTagCompound json)
	{
		BrewingRecipeHC r = new BrewingRecipeHC();
		
		r.input = loadStacks(json.getTag("input"), "input").get(0);
		r.ingredients.addAll(loadStacks(json.getTag("ingredients"), "ingredients"));
		r.output = loadStack(json.getCompoundTag("output"));
		
		return r;
	}
	
	@Override
	public void addRecipe(BrewingRecipeHC recipe)
	{
		ItemMatchParams params = new ItemMatchParams().setUseOredict(true);
		recipe.recipeInstance = BrewingRecipe.INSTANCE.addRecipe(ItemListContainerHelper.stackPredicate(Arrays.asList(recipe.input), params), ItemListContainerHelper.stackPredicate(recipe.ingredients, params), recipe.output);
	}
	
	@Override
	public void removeRecipe(BrewingRecipeHC recipe)
	{
		BrewingRecipe.INSTANCE.recipes.remove(recipe.recipeInstance);
	}
}