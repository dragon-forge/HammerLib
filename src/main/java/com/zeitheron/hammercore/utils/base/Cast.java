package com.zeitheron.hammercore.utils.base;

import java.util.Optional;

public class Cast
{
	public static <T> Optional<T> optionally(Object obj, Class<T> to)
	{
		return Optional.ofNullable(cast(obj, to));
	}

	public static <T> T cast(Object v)
	{
		try
		{
			return (T) v;
		} catch(Throwable err)
		{
			return null;
		}
	}

	public static <T> T cast(Object obj, Class<T> to)
	{
		if(obj != null && to.isAssignableFrom(obj.getClass()))
			return to.cast(obj);
		return null;
	}
}