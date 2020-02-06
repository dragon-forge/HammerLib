package com.zeitheron.hammercore.client.utils.gl;

@FunctionalInterface
public interface IGLBufferStream<T>
{
	void put(T value);

	default void putAll(T... values)
	{
		for(T v : values) put(v);
	}
}