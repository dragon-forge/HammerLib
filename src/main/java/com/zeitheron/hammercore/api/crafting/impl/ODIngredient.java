package com.zeitheron.hammercore.api.crafting.impl;

import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;

public class ODIngredient extends MCIngredient
{
	public final String ore;
	
	public ODIngredient(String ore)
	{
		super(new OreIngredient(ore));
		this.ore = ore;
	}
	
	@Override
	public Ingredient getCopy()
	{
		return new OreIngredient(ore);
	}
}