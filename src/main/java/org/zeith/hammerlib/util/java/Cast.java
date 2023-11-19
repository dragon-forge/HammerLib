package org.zeith.hammerlib.util.java;

import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.*;

public class Cast
{
	public static BooleanSupplier constantB(boolean value)
	{
		return () -> value;
	}
	
	public static DoubleSupplier constantD(double value)
	{
		return () -> value;
	}
	
	public static IntSupplier constantI(int value)
	{
		return () -> value;
	}
	
	public static LongSupplier constantL(long value)
	{
		return () -> value;
	}
	
	@Deprecated
	public static <T> Supplier<T> staticValue(T value)
	{
		return () -> value;
	}
	
	public static <T> Supplier<T> constant(T value)
	{
		return () -> value;
	}
	
	/**
	 * Unwraps a supplier of a supplier and returns the inner supplier's value.
	 *
	 * <p>This method is useful for unwrapping a supplier of a supplier that has been returned by a method
	 * such as {@link org.zeith.hammerlib.compat.base.sided.SidedAbilityBase#client()}.
	 *
	 * @param suppl
	 * 		the supplier of a supplier to unwrap
	 * @param <T>
	 * 		the type of the inner supplier's value
	 *
	 * @return the inner supplier's value
	 */
	public static <T> T get2(Supplier<Supplier<T>> suppl)
	{
		return suppl.get().get();
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
	
	public static <IN, T> Function<IN, T> convertTo(Class<T> type)
	{
		return obj -> cast(obj, type);
	}
	
	public static <T> Optional<T> firstInstanceof(Class<T> type, Object... input)
	{
		for(Object i : input)
			if(i != null && type.isAssignableFrom(i.getClass()))
				return Optional.of(type.cast(i));
		return Optional.empty();
	}
	
	public static <T> T or(T... input)
	{
		for(var i : input)
			if(i != null)
				return i;
		return null;
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
	
	@Contract("null -> null; _ -> _")
	public static <T> T cast(Object thing)
	{
		return (T) thing;
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
	
	public static <T> T newInstanceWithRE(Class<T> type)
			throws RuntimeException
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
			throw new RuntimeException(err);
		}
	}
}