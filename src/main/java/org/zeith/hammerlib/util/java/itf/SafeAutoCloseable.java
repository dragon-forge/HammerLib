package org.zeith.hammerlib.util.java.itf;

public interface SafeAutoCloseable
		extends AutoCloseable
{
	@Override
	void close();
}