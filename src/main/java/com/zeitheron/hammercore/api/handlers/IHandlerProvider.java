package com.zeitheron.hammercore.api.handlers;

import net.minecraft.util.EnumFacing;

/**
 * Almost the same as capabilities. Used for Advanced Multipart usage like
 * cables.
 */
public interface IHandlerProvider
{
	<T extends ITileHandler> T getHandler(EnumFacing facing, Class<T> handler, Object... params);
	
	<T extends ITileHandler> boolean hasHandler(EnumFacing facing, Class<T> handler, Object... params);
}