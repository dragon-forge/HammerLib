package com.zeitheron.hammercore.utils.charging;

public abstract class AbstractCharge
{
	/**
	 * @return true if there is any more charge that can be applied to stack,
	 *         false otherwise.
	 */
	public abstract boolean containsCharge();
	
	/**
	 * Creates a copy of this charge with it's current charge.
	 */
	public abstract AbstractCharge copy();
}