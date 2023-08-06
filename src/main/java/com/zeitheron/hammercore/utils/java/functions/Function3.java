package com.zeitheron.hammercore.utils.java.functions;

import java.util.function.Function;

@FunctionalInterface
public interface Function3<A, B, C, RES>
{
	RES apply(A a, B b, C c);
	
	default <RES_MAPPED> Function3<A, B, C, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c) -> mapper.apply(apply(a, b, c));
	}
}