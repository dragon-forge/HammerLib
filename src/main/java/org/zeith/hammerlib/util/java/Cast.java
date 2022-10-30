package org.zeith.hammerlib.util.java;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Supplier;

public class Cast
{
	public static <T> Supplier<T> staticValue(T value)
	{
		return () -> value;
	}
	
	public static <T> Supplier<T> supply(Object thing, Class<T> type)
	{
		T result = cast(thing, type);
		return () -> result;
	}
	
	@Nonnull
	public static <T> Optional<T> optionally(Object thing, Class<T> type)
	{
		return type.isInstance(thing) ? Optional.of(type.cast(thing)) : Optional.empty();
	}
	
	public static <T> Optional<T> firstInstanceof(Class<T> type, Object... input)
	{
		for(Object i : input)
			if(i != null && type.isAssignableFrom(i.getClass()))
				return Optional.of(type.cast(i));
		return Optional.empty();
	}
	
	@Nullable
	public static <T> T cast(Object thing, Class<T> type)
	{
		if(thing == null || type == null)
			return null;
		if(type.isAssignableFrom(thing.getClass()))
			return type.cast(thing);
		return null;
	}
	
	public static <T> T cast(Object thing)
	{
		try
		{
			return (T) thing;
		} catch(Throwable err)
		{
			return null;
		}
	}
	
	static final Map<Class, Constructor> emptyCtors = new HashMap<>();
	
	public static <T> Supplier<T> newInstanceSupplier(Class<T> type)
	{
		return () -> newInstance(type);
	}
	
	public static <T> T newInstance(Class<T> type)
	{
		try
		{
			Constructor<T> gen = emptyCtors.get(type);
			if(gen == null)
			{
				gen = type.getDeclaredConstructor();
				gen.setAccessible(true);
				emptyCtors.put(type, gen);
			}
			return gen.newInstance();
		} catch(Throwable err)
		{
			return null;
		}
	}
}