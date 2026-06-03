package com.zeitheron.hammercore.client.utils.gl;

import java.nio.FloatBuffer;

@FunctionalInterface
public interface IGLFloatBufferStream
{
	void put(float value);
	
	default void putAll(float... values)
	{
		for(float v : values) put(v);
	}
	
	static IGLFloatBufferStream forBuffer(FloatBuffer fb)
	{
		return new IGLFloatBufferStream()
		{
			@Override
			public void put(float value)
			{
				fb.put(value);
			}
			
			@Override
			public void putAll(float... values)
			{
				fb.put(values);
			}
		};
	}
}