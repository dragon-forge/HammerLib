package org.zeith.hammerlib.util.configured.io;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class UnsafeHax
{
	@SuppressWarnings("unchecked")
	public static <T> T cast(Object anything)
	{
		return (T) anything;
	}
	
	public static String toString(Object[] array)
	{
		return Arrays.stream(array)
				.map(o -> o != null && o.getClass().isArray() ? toString((Object[]) o) : Objects.toString(o))
				.collect(Collectors.joining(", ", "[", "]"));
	}
}