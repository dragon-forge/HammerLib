package com.zeitheron.hammercore.api.crafting.impl;

import com.zeitheron.hammercore.api.crafting.IFluidIngredient;
import com.zeitheron.hammercore.api.crafting.IngredientStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import java.util.Collections;
import java.util.List;

/**
 * The fluid ingredient implementation.
 */
public class FluidStackIngredient implements IFluidIngredient<FluidStackIngredient>
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
	public List<FluidStack> asIngredient()
	{
		return Collections.singletonList(stack != null ? stack.copy() : null);
	}
}