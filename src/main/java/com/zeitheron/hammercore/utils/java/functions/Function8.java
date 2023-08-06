package com.zeitheron.hammercore.utils.java.functions;

import java.util.function.Function;

@FunctionalInterface
public interface Function8<A, B, C, D, E, F, G, H, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f, G g, H h);
	
	default <RES_MAPPED> Function8<A, B, C, D, E, F, G, H, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f, g, h) -> mapper.apply(apply(a, b, c, d, e, f, g, h));
	}
}