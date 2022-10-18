package org.zeith.hammerlib.api.crafting.impl;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.zeith.hammerlib.api.crafting.IFluidIngredient;
import org.zeith.hammerlib.api.crafting.IngredientStack;
import org.zeith.hammerlib.util.mcf.fluid.FluidIngredient;

import java.util.List;

/**
 * The fluid ingredient implementation.
 */
public record FluidStackIngredient(FluidIngredient ingredient)
		implements IFluidIngredient<FluidStackIngredient>
{
	@Override
	public boolean canTakeFrom(IFluidTank tank, IngredientStack<FluidStackIngredient> stack)
	{
		int total = stack.amount;
		FluidStack drained = tank.drain(total, IFluidHandler.FluidAction.SIMULATE);
		return !drained.isEmpty() && stack.ingredient.ingredient.test(drained) && drained.getAmount() >= total;
	}
	
	@Override
	public boolean takeFrom(IFluidTank tank, IngredientStack<FluidStackIngredient> stack)
	{
		if(canTakeFrom(tank, stack))
		{
			int total = stack.amount;
			FluidStack drained = tank.drain(total, IFluidHandler.FluidAction.EXECUTE);
			return !drained.isEmpty() && stack.ingredient.ingredient.test(drained) && drained.getAmount() >= total;
		}
		
		return false;
	}
	
	@Override
	public List<FluidStack> asIngredient()
	{
		return List.of(ingredient.getValues());
	}
	
	@Override
	public List<FluidStack> asIngredient(IngredientStack<FluidStackIngredient> stack)
	{
		return List.of(stack.ingredient.ingredient.getValues(stack.amount));
	}
}