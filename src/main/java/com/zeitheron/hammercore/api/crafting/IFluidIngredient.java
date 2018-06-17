package com.zeitheron.hammercore.api.crafting;

import net.minecraftforge.fluids.IFluidTank;

public interface IFluidIngredient<T extends IFluidIngredient> extends IBaseIngredient
{
	boolean canTakeFrom(IFluidTank tank, IngredientStack<T> stack);
	
	boolean takeFrom(IFluidTank tank, IngredientStack<T> stack);
}