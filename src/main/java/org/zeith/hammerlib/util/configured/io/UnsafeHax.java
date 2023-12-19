package org.zeith.hammerlib.util.configured.io;

import net.minecraftforge.fml.unsafe.UnsafeHacks;

import java.util.*;
import java.util.stream.Collectors;

public class UnsafeHax
{
	/**
	 * Creates a new instance without calling constructor < init> method.
	 */
	public static <T> T unitializedInstance(Class<T> type)
	{
		return UnsafeHacks.newInstance(type);
	}
	
	public static String toString(Object[] array)
	{
		return Arrays.stream(array)
				.map(o -> o != null && o.getClass().isArray() ? toString((Object[]) o) : Objects.toString(o))
				.collect(Collectors.joining(", ", "[", "]"));
	}
}