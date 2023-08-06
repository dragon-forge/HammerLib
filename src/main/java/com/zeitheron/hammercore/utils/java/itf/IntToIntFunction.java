package com.zeitheron.hammercore.utils.java.itf;

@FunctionalInterface
public interface IntToIntFunction
{
	/**
	 * Applies this function to the given argument.
	 *
	 * @param value
	 * 		the function argument
	 *
	 * @return the function result
	 */
	int applyAsInt(int value);
}