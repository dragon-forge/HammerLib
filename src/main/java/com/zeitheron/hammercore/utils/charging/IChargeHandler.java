package com.zeitheron.hammercore.utils.charging;

import net.minecraft.item.ItemStack;

public interface IChargeHandler<T extends AbstractCharge>
{
	/**
	 * @return the id that this handler may be found with
	 */
	String getId();
	
	/**
	 * @param stack
	 *            the stack being checked
	 * @return true if the given stack may be charged, false otherwise.
	 */
	boolean canCharge(ItemStack stack, T charge);
	
	/**
	 * Charges the given stack, if not simulating, and returns the leftover
	 * charge that wasn't used.
	 * 
	 * @param stack
	 *            the stack being charged.
	 * @param charge
	 *            the charge being applied to stack.
	 * @param simulate
	 *            if false, then this action should modify the stack.
	 * @return leftover charge that may be calculated like so:
	 *         <code>charge - consumed</code>
	 */
	T charge(ItemStack stack, T charge, boolean simulate);
}