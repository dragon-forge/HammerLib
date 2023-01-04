package org.zeith.hammerlib.util.java.functions;

import java.util.function.Function;

@FunctionalInterface
public interface Function11<A, B, C, D, E, F, G, H, I, J, K, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k);
	
	default <RES_MAPPED> Function11<A, B, C, D, E, F, G, H, I, J, K, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> mapper.apply(apply(a, b, c, d, e, f, g, h, i, j, k));
	}
}