package com.zeitheron.hammercore.utils.java.functions;

import java.util.function.Function;

@FunctionalInterface
public interface Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p);
	
	default <RES_MAPPED> Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) -> mapper.apply(apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p));
	}
}