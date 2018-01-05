package com.pengu.hammercore.recipeAPI.vanilla.furnace;

import com.pengu.hammercore.recipeAPI.iRecipeType;

import mezz.jei.plugins.vanilla.furnace.SmeltingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.oredict.OreDictionary;

public class SmeltingRecipeType implements iRecipeType<SmeltingRecipeHC>
{
	@Override
	public boolean isJeiSupported(SmeltingRecipeHC recipe)
	{
		return true;
	}
	
	@Override
	public Object getJeiRecipeFor(SmeltingRecipeHC recipe, boolean remove)
	{
		return new SmeltingRecipe(recipe.input, recipe.output);
	}
	
	@Override
	public String getTypeId()
	{
		return "smelting";
	}
	
	@Override
	public SmeltingRecipeHC createRecipe(NBTTagCompound json)
	{
		SmeltingRecipeHC recipe = new SmeltingRecipeHC();
		
		String item = "input";
		
		if(json.hasKey(item, NBT.TAG_STRING))
			recipe.input.addAll(OreDictionary.getOres(json.getString(item)));
		else if(json.hasKey(item, NBT.TAG_COMPOUND))
			recipe.input.add(loadStack(json.getCompoundTag(item)));
		else
			throw new RecipeParseException("Undefined type for ingredient '" + item + "': TagID: " + json.getTagId(item) + ", Content: " + json.getTag(item));
		
		item = "output";
		if(json.hasKey(item, NBT.TAG_COMPOUND))
			recipe.output = loadStack(json.getCompoundTag(item));
		else
			throw new RecipeParseException("Undefined type for ingredient '" + item + "': TagID: " + json.getTagId(item) + ", Content: " + json.getTag(item));
		
		return recipe;
	}
	
	@Override
	public void addRecipe(SmeltingRecipeHC recipe)
	{
		for(ItemStack in : recipe.input)
			FurnaceRecipes.instance().addSmeltingRecipe(in, recipe.output, 0);
	}
	
	@Override
	public void removeRecipe(SmeltingRecipeHC recipe)
	{
		for(ItemStack in : recipe.input)
			FurnaceRecipes.instance().getSmeltingList().remove(in);
	}
	
	@Override
	public void addOnUnload(SmeltingRecipeHC recipe)
	{
		for(ItemStack in : recipe.input)
			FurnaceRecipes.instance().addSmeltingRecipe(in, recipe.output, 0);
	}
	
	@Override
	public void removeOnLoad(SmeltingRecipeHC recipe)
	{
		for(ItemStack in : recipe.input)
			FurnaceRecipes.instance().getSmeltingList().remove(in);
	}
}