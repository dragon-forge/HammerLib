package org.zeith.api.blocks.redstone;

import org.jetbrains.annotations.Range;

/**
 * Capability interface for representing a redstone bundle.
 * Redstone bundles are capable of storing and retrieving redstone strengths for different colors.
 */
public interface IRedstoneBundle
		extends IRedstoneBundleAccessor
{
	/**
	 * Checks if the redstone bundle is connected.
	 *
	 * @return {@code true} if the redstone bundle is connected, {@code false} otherwise.
	 */
	boolean isConnected();
	
	/**
	 * Sets the serialized bundle signal.
	 *
	 * @param serialized
	 * 		The serialized bundle signal value to set.
	 *
	 * @return {@code true} if the operation was successful, {@code false} otherwise.
	 */
	boolean setSerializedBundleSignal(@Range(from = 0, to = 65535) int serialized);
	
	/**
	 * Sets the redstone strength for the specified color.
	 *
	 * @param color
	 * 		The color of the redstone wire.
	 * @param signal
	 * 		The redstone presence to set.
	 *
	 * @return {@code true} if the operation was successful, {@code false} otherwise.
	 */
	default boolean setSignal(MCColor color, boolean signal)
	{
		var s = getSerializedBundleSignal();
		var has = IRedstoneBundleAccessor.hasSerialized(s, color);
		
		if(signal) s = IRedstoneBundleAccessor.add(s, color);
		else s = IRedstoneBundleAccessor.remove(s, color);
		
		return setSerializedBundleSignal(s);
	}
}