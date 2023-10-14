package com.zeitheron.hammercore.api.handlers;

import com.zeitheron.hammercore.utils.base.Cast;

/**
 * Abstract handler that can contain any method you need.
 */
public interface ITileHandler
{
	default <T extends ITileHandler> T get(Class<T> handler)
	{
		return Cast.cast(this, handler);
	}
}