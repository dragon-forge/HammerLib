package com.pengu.hammercore.api.handlers;

import net.minecraft.util.EnumFacing;

/**
 * Almost the same as capabilities. Used for Advanced Multipart usage like
 * cables.
 */
public interface iHandlerProvider
{
	<T extends iTileHandler> T getHandler(EnumFacing facing, Class<T> handler, Object... params);
	
	<T extends iTileHandler> boolean hasHandler(EnumFacing facing, Class<T> handler, Object... params);
}