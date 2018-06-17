package com.zeitheron.hammercore.api.crafting;

import net.minecraft.inventory.IInventory;

public interface IItemIngredient<T extends IItemIngredient> extends IBaseIngredient
{
	boolean canTakeFrom(IInventory inv, IngredientStack<T> stack);
	
	boolean takeFrom(IInventory inv, IngredientStack<T> stack);
}