package com.zeitheron.hammercore.api.crafting.impl;

import com.zeitheron.hammercore.api.crafting.ICustomIngredient;
import com.zeitheron.hammercore.api.crafting.IFluidIngredient;
import com.zeitheron.hammercore.api.crafting.IngredientStack;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public class FluidStackIngredient implements ICustomIngredient<FluidStack>, IFluidIngredient<FluidStackIngredient>
{
	public FluidStack stack;
	
	public FluidStackIngredient(FluidStack stack)
	{
		this.stack = stack;
	}
	
	@Override
	public boolean canTakeFrom(IFluidTank tank, IngredientStack<FluidStackIngredient> stack)
	{
		int total = stack.amount * stack.ingredient.stack.amount;
		FluidStack drained = tank.drain(total, false);
		return drained != null && drained.getFluid() == stack.ingredient.stack.getFluid() && drained.amount >= total;
	}
	
	@Override
	public boolean takeFrom(IFluidTank tank, IngredientStack<FluidStackIngredient> stack)
	{
		if(canTakeFrom(tank, stack))
		{
			int total = stack.amount * stack.ingredient.stack.amount;
			FluidStack drained = tank.drain(total, true);
			return drained != null && drained.getFluid() == stack.ingredient.stack.getFluid() && drained.amount >= total;
		}
		
		return false;
	}
	
	@Override
	public FluidStack getCopy()
	{
		return stack.copy();
	}
	
	@Override
	public FluidStack getOrigin()
	{
		return stack;
	}
}