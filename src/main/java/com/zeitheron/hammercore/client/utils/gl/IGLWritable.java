package com.zeitheron.hammercore.client.utils.gl;

public interface IGLWritable
{
	int getFloatSize();

	void writeFloats(IGLBufferStream<Float> stream);
}