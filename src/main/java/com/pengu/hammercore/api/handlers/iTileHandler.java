package com.pengu.hammercore.api.handlers;

import com.pengu.hammercore.common.utils.WorldUtil;

/**
 * Abstract handler that can contain any method you need.
 */
public interface iTileHandler
{
	default <T extends iTileHandler> T get(Class<T> handler)
	{
		return WorldUtil.cast(this, handler);
	}
}