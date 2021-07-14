package org.zeith.hammerlib.api.crafting.impl;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.zeith.hammerlib.api.crafting.IFluidIngredient;
import org.zeith.hammerlib.api.crafting.IngredientStack;

import java.util.Collections;
import java.util.List;

/**
 * The fluid ingredient implementation.
 */
public class FluidStackIngredient
		implements IFluidIngredient<FluidStackIngredient>
{
	public FluidStack stack;

	public FluidStackIngredient(FluidStack stack)
	{
		this.stack = stack;
	}

	@Override
	public boolean canTakeFrom(IFluidTank tank, IngredientStack<FluidStackIngredient> stack)
	{
		int total = stack.amount * stack.ingredient.stack.getAmount();
		FluidStack drained = tank.drain(total, IFluidHandler.FluidAction.SIMULATE);
		return !drained.isEmpty() && drained.getFluid() == stack.ingredient.stack.getFluid() && drained.getAmount() >= total;
	}

	@Override
	public boolean takeFrom(IFluidTank tank, IngredientStack<FluidStackIngredient> stack)
	{
		if(canTakeFrom(tank, stack))
		{
			int total = stack.amount * stack.ingredient.stack.getAmount();
			FluidStack drained = tank.drain(total, IFluidHandler.FluidAction.EXECUTE);
			return !drained.isEmpty() && drained.getFluid() == stack.ingredient.stack.getFluid() && drained.getAmount() >= total;
		}

		return false;
	}

	@Override
	public List<FluidStack> asIngredient()
	{
		return Collections.singletonList(stack != null ? stack.copy() : null);
	}
}