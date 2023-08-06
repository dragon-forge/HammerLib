package com.zeitheron.hammercore.utils.java.functions;

import java.util.function.Function;

@FunctionalInterface
public interface Function12<A, B, C, D, E, F, G, H, I, J, K, L, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l);
	
	default <RES_MAPPED> Function12<A, B, C, D, E, F, G, H, I, J, K, L, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> mapper.apply(apply(a, b, c, d, e, f, g, h, i, j, k, l));
	}
}