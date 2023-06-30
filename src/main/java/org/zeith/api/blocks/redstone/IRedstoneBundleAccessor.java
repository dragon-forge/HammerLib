package org.zeith.api.blocks.redstone;

import org.jetbrains.annotations.Range;

/**
 * Provides access to redstone bundle signals.
 */
@FunctionalInterface
public interface IRedstoneBundleAccessor
{
	/**
	 * Returns the serialized bundle value of signals provided by this accessor.
	 *
	 * @return The serialized bundle signal value.
	 */
	@Range(from = 0, to = 65535)
	int getSerializedBundleSignal();
	
	
	/**
	 * Retrieves the redstone strength for the specified color.
	 *
	 * @param color
	 * 		The color of the redstone wire.
	 *
	 * @return {@code true} if the specified color has a redstone signal, {@code false} otherwise.
	 */
	default boolean hasSignal(MCColor color)
	{
		return hasSerialized(getSerializedBundleSignal(), color);
	}
	
	/**
	 * Checks if the specified color has a redstone signal based on the serialized bundle signal.
	 *
	 * @param colors
	 * 		The serialized bundle signal.
	 * @param color
	 * 		The color of the redstone wire.
	 *
	 * @return {@code true} if the specified color has a redstone signal, {@code false} otherwise.
	 */
	static boolean hasSerialized(int colors, MCColor color)
	{
		int mask = color.bitmask;
		return (colors & mask) == mask;
	}
	
	/**
	 * Returns the serialized bundle signal by checking each color's redstone presence.
	 *
	 * @param bundle
	 * 		The redstone bundle accessor.
	 *
	 * @return The serialized bundle signal value.
	 */
	static int getSerializedBundleSignal(IRedstoneBundleAccessor bundle)
	{
		int r = 0;
		for(var color : MCColor.values())
			if(bundle.hasSignal(color))
				r |= color.bitmask;
		return r;
	}
	
	/**
	 * Adds the specified colors to the serialized bundle signal.
	 *
	 * @param serialized
	 * 		The current serialized bundle signal.
	 * @param colors
	 * 		The colors to add.
	 *
	 * @return The updated serialized bundle signal.
	 */
	static int add(int serialized, MCColor... colors)
	{
		for(var color : colors)
			serialized |= color.bitmask;
		return serialized;
	}
	
	/**
	 * Removes the specified colors from the serialized bundle signal.
	 *
	 * @param serialized
	 * 		The current serialized bundle signal.
	 * @param colors
	 * 		The colors to remove.
	 *
	 * @return The updated serialized bundle signal.
	 */
	static int remove(int serialized, MCColor... colors)
	{
		for(var color : colors)
			serialized &= ~color.bitmask;
		return serialized;
	}
}