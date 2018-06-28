package com.zeitheron.hammercore.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

public class ArrayHelper
{
	public static <T, R> R[] map(Function<T, R> func, T... ts)
	{
		return collect(stream(ts).map(func));
	}
	
	public static <T, R> List<R> map(Function<T, R> func, List<T> ts)
	{
		return ts.stream().map(func).collect(Collectors.toList());
	}
	
	public static <T> Stream<T> stream(T... ts)
	{
		return Arrays.stream(ts);
	}
	
	public static <T> T[] collect(Stream<T> str)
	{
		return (T[]) str.collect(Collectors.toList()).toArray();
	}
	
	public static <T> T[] newArray(Class<T> type, int length)
	{
		return (T[]) Array.newInstance(type, length);
	}
	
	public static <T> T[] merge(T[] a, T... ts)
	{
		return ArrayUtils.addAll(a, ts);
	}
	
	public static <T> T[] merge(T[] a, T[]... tmat)
	{
		for(T[] ts : tmat)
			a = merge(a, ts);
		return a;
	}
}