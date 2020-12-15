package com.zeitheron.hammercore.api.crafting;

import net.minecraft.item.ItemStack;

public interface ICraftingResult<R>
{
	/**
	 * Get the output in context  of an executor
	 */
	default R getOutput(ICraftingExecutor executor)
	{
		return getBaseOutput();
	}
	
	/**
	 * Gets all components of the result.
	 * Return array, consisting of {@link ItemStack}, {@link net.minecraftforge.fluids.FluidStack}
	 * and {@link com.zeitheron.hammercore.utils.charging.fe.FECharge}. Other objects might not map.
	 */
	default Object[] getAllResults(ICraftingExecutor executor)
	{
		return new Object[]{getOutput(executor)};
	}
	
	/**
	 * Gets the basic output, produced by this result.
	 * Used for things like EMC mapping by Expanded Equivalence to take place.
	 */
	R getBaseOutput();
	
	/**
	 * Get the raw type of the crafting result.
	 */
	Class<R> getType();
	
	/**
	 * Does this result have a sensible base output?
	 */
	default boolean hasBaseOutput()
	{
		R out = getBaseOutput();
		if(out == null) return false;
		return !(out instanceof ItemStack) || !((ItemStack) out).isEmpty();
	}
}