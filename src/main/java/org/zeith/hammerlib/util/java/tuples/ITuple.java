package org.zeith.hammerlib.util.java.tuples;

import java.util.stream.Stream;

public interface ITuple
{
	int arity();
	
	Stream<?> stream();
}