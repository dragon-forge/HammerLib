package org.zeith.hammerlib.util.java.functions;

import java.util.function.Function;

@FunctionalInterface
public interface Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o);
	
	default <RES_MAPPED> Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> mapper.apply(apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o));
	}
}