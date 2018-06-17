package com.zeitheron.hammercore.api.crafting.impl;

import com.zeitheron.hammercore.api.crafting.ICustomIngredient;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;

public class OreDictIngredient extends MCIngredient
{
	public final String ore;
	
	public OreDictIngredient(String ore)
	{
		super(new OreIngredient(ore));
		this.ore = ore;
	}
	
	@Override
	public Ingredient copy()
	{
		return new OreIngredient(ore);
	}
}