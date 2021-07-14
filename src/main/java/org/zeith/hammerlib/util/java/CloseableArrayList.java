package org.zeith.hammerlib.util.java;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;

public class CloseableArrayList<T extends Closeable>
		extends ArrayList<T>
		implements AutoCloseable
{
	public CloseableArrayList()
	{
	}

	public CloseableArrayList(int initialCapacity)
	{
		super(initialCapacity);
	}

	public CloseableArrayList(Collection<? extends T> c)
	{
		super(c);
	}

	@Override
	public void close() throws Exception
	{
		for(T t : this) t.close();
	}
}