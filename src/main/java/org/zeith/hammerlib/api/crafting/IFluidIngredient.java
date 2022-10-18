package org.zeith.hammerlib.api.crafting;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import java.util.List;

public interface IFluidIngredient<T extends IFluidIngredient<T>>
		extends IBaseIngredient
{
	/**
	 * Traces if it is possible to take ingredient's fluid content.
	 *
	 * @param tank
	 * 		the {@link IFluidTank} to be tested
	 * @param stack
	 * 		the stack that will be attempted to extract
	 *
	 * @return if the content can be taken.
	 */
	boolean canTakeFrom(IFluidTank tank, IngredientStack<T> stack);
	
	/**
	 * Tries to extract the fluid from the tank.
	 *
	 * @param tank
	 * 		the {@link IFluidTank} to be emptied
	 * @param stack
	 * 		the stack that will be attempted to extract
	 *
	 * @return if the content was taken.
	 */
	boolean takeFrom(IFluidTank tank, IngredientStack<T> stack);
	
	/**
	 * @return The list of fluids represented in this ingredient that can be taken.
	 * The amount of fluid in every entry of the list is NOT set to the correct amount required by the recipe
	 * (see {@link #asIngredient(IngredientStack)}) version of this method.
	 */
	@Deprecated(forRemoval = true)
	default List<FluidStack> asIngredient()
	{
		return List.of();
	}
	
	default List<FluidStack> asIngredient(IngredientStack<T> stack)
	{
		return stack.ingredient.asIngredient();
	}
}