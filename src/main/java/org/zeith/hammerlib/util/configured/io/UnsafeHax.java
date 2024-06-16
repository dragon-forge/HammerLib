package org.zeith.hammerlib.util.configured.io;

import com.google.gson.internal.UnsafeAllocator;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class UnsafeHax
{
	/**
	 * Creates a new instance without calling constructor < init> method.
	 */
	@SneakyThrows
	public static <T> T unitializedInstance(Class<T> type)
	{
		return UnsafeAllocator.INSTANCE.newInstance(type);
	}
	
	public static String toString(Object[] array)
	{
		return Arrays.stream(array)
				.map(o -> o != null && o.getClass().isArray() ? toString((Object[]) o) : Objects.toString(o))
				.collect(Collectors.joining(", ", "[", "]"));
	}
}