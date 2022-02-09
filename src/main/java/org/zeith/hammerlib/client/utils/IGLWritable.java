package org.zeith.hammerlib.client.utils;

public interface IGLWritable
{
	int getFloatSize();

	void writeFloats(IGLBufferStream<Float> stream);
}