package com.zeitheron.hammercore.utils.java.tuples;

import java.util.stream.Stream;

public interface ITuple
{
	int arity();
	
	Stream<?> stream();
}