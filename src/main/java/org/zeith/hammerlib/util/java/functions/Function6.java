package org.zeith.hammerlib.util.java.functions;

import java.util.function.Function;

@FunctionalInterface
public interface Function6<A, B, C, D, E, F, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f);
	
	default <RES_MAPPED> Function6<A, B, C, D, E, F, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f) -> mapper.apply(apply(a, b, c, d, e, f));
	}
}