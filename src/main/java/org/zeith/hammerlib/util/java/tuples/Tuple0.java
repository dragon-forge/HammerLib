package org.zeith.hammerlib.util.java.tuples;

import java.util.stream.Stream;

public final class Tuple0
		implements ITuple
{
	public static final Tuple0 INSTANCE = new Tuple0();
	
	@Override
	public int arity()
	{
		return 0;
	}
	
	@Override
	public Stream<?> stream()
	{
		return Stream.empty();
	}
}