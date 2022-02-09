package org.zeith.hammerlib.client.utils;

@FunctionalInterface
public interface IGLBufferStream<T>
{
	void put(T value);

	default void putAll(T... values)
	{
		for(T v : values) put(v);
	}
}