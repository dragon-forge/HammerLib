package org.zeith.hammerlib.util.java.functions;

import java.util.function.Function;

@FunctionalInterface
public interface Function10<A, B, C, D, E, F, G, H, I, J, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j);
	
	default <RES_MAPPED> Function10<A, B, C, D, E, F, G, H, I, J, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> mapper.apply(apply(a, b, c, d, e, f, g, h, i, j));
	}
}