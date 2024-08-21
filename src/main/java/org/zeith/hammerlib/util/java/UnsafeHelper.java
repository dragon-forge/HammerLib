package org.zeith.hammerlib.util.java;

import com.google.gson.internal.UnsafeAllocator;
import com.zeitheron.hammercore.HammerCore;

import java.util.function.Supplier;

public class UnsafeHelper
{
	private static final UnsafeAllocator allocator;
	
	static
	{
		UnsafeAllocator a;
		try
		{
			a = (UnsafeAllocator) UnsafeAllocator.class.getDeclaredField("INSTANCE").get(null);
			HammerCore.LOG.info("Detected more modern version of GSON, using UnsafeAllocator.INSTANCE");
		} catch(Throwable e)
		{
			try
			{
				a = UnsafeAllocator.create();
				HammerCore.LOG.info("Detected older version of GSON, using UnsafeAllocator.create()");
			} catch(Throwable e2)
			{
				a = null;
				HammerCore.LOG.info("Detected corrupted version of GSON, can't use UnsafeAllocator :(");
			}
		}
		allocator = a;
	}
	
	public static <T> T newInstance(Class<T> type)
	{
		try
		{
			return allocator.newInstance(type);
		} catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static <T> Supplier<T> factory(Class<T> type)
	{
		return () -> newInstance(type);
	}
}