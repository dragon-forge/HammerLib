package org.zeith.hammerlib.util.java.functions;

import java.util.function.Function;

@FunctionalInterface
public interface Function5<A, B, C, D, E, RES>
{
	RES apply(A a, B b, C c, D d, E e);
	
	default <RES_MAPPED> Function5<A, B, C, D, E, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e) -> mapper.apply(apply(a, b, c, d, e));
	}
}