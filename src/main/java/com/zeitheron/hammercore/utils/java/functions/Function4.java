package com.zeitheron.hammercore.utils.java.functions;

import java.util.function.Function;

@FunctionalInterface
public interface Function4<A, B, C, D, RES>
{
	RES apply(A a, B b, C c, D d);
	
	default <RES_MAPPED> Function4<A, B, C, D, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d) -> mapper.apply(apply(a, b, c, d));
	}
}