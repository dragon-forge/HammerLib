package org.zeith.hammerlib.util.java;

public interface SafeCloseable
		extends AutoCloseable
{
	@Override
	void close();
}