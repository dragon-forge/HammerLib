package com.zeitheron.hammercore.utils.java.functions;

import java.util.function.Function;

@FunctionalInterface
public interface Function9<A, B, C, D, E, F, G, H, I, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f, G g, H h, I i);
	
	default <RES_MAPPED> Function9<A, B, C, D, E, F, G, H, I, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f, g, h, i) -> mapper.apply(apply(a, b, c, d, e, f, g, h, i));
	}
}