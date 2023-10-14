package com.zeitheron.hammercore.utils.java.itf;

@FunctionalInterface
public interface BooleanConsumer
{
	void accept(boolean value);
	
	default Runnable fill(boolean value)
	{
		return () -> accept(value);
	}
}