package org.zeith.hammerlib.api.crafting;

import net.minecraft.item.ItemStack;
import org.zeith.hammerlib.util.charging.fe.FECharge;

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
	 * and {@link FECharge}. Other objects might not map.
	 */
	default Object[] getAllOutputs(ICraftingExecutor executor)
	{
		return new Object[]{ getOutput(executor) };
	}

	/**
	 * Gets all components of the result.
	 * Return array, consisting of {@link ItemStack}, {@link net.minecraftforge.fluids.FluidStack}
	 * and {@link FECharge}. Other objects might not map.
	 */
	default Object[] getAllBaseOutputs()
	{
		return hasBaseOutput() ? new Object[]{ getBaseOutput() } : new Object[0];
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