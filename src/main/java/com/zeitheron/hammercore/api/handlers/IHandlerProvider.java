package com.zeitheron.hammercore.api.handlers;

import net.minecraft.util.EnumFacing;

/**
 * Almost the same as capabilities. Used for Advanced Multipart usage like
 * cables.
 */
public interface IHandlerProvider
{
	/**
	 * Gets the handler for the passed class, at facing with params.
	 */
	<T extends ITileHandler> T getHandler(EnumFacing facing, Class<T> handler, Object... params);
	
	/**
	 * Tests if the handler for the passed class, at facing with params exists.
	 */
	<T extends ITileHandler> boolean hasHandler(EnumFacing facing, Class<T> handler, Object... params);
}