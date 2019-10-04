package com.zeitheron.hammercore.utils.base;

import com.zeitheron.hammercore.utils.WorldUtil;

public class Cast
{
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
			return (T) obj;
		return null;
	}
}