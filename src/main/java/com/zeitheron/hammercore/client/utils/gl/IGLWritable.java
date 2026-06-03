package com.zeitheron.hammercore.client.utils.gl;

public interface IGLWritable
{
	int getFloatSize();
	
	void writeFloats(IGLFloatBufferStream stream);
	
	@Deprecated
	default void writeFloats(IGLBufferStream<Float> stream)
	{
		IGLFloatBufferStream fbs = stream::put;
		writeFloats(fbs);
	}
}