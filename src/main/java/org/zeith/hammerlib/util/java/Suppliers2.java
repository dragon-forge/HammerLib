package org.zeith.hammerlib.util.java;

import com.google.common.base.MoreObjects;
import org.zeith.hammerlib.client.flowgui.reader.ComDrivers;
import org.zeith.hammerlib.util.java.itf.FloatSupplier;

import java.util.function.*;

// 2 suffix because we have com.google.common.base.Suppliers already
public class Suppliers2
{
	public static <I, O> Supplier<O> map(Supplier<I> input, Function<I, O> mapper)
	{
		if(input == null && mapper == null) return null;
		
		// Optimize constant mapping to happen just once
		if(ComDrivers.isConstant(input)) return Cast.constant(mapper.apply(input.get()));
		
		return () -> mapper.apply(input.get());
	}
	
	public static <I> IntSupplier mapToInt(Supplier<I> input, ToIntFunction<I> mapper)
	{
		return () -> mapper.applyAsInt(input.get());
	}
	
	public static <I> FloatSupplier mapToFloat(Supplier<I> input, Function<I, Float> mapper)
	{
		return () -> MoreObjects.firstNonNull(mapper.apply(input.get()), Float.NaN);
	}
	
	public static <I> DoubleSupplier mapToDouble(Supplier<I> input, ToDoubleFunction<I> mapper)
	{
		return () -> mapper.applyAsDouble(input.get());
	}
	
	public static <I> LongSupplier mapToLong(Supplier<I> input, ToLongFunction<I> mapper)
	{
		return () -> mapper.applyAsLong(input.get());
	}
}