package org.zeith.hammerlib.util.charging;

public abstract class AbstractCharge
{
	/**
	 * @return true if there is any more charge that can be applied to stack,
	 * false otherwise.
	 */
	public abstract boolean containsCharge();

	/**
	 * Creates a copy of this charge with it's current charge.
	 *
	 * @return A copy of this charge with it's current charge.
	 */
	public abstract AbstractCharge copy();
}