package org.zeith.hammerlib.util.java.functions;

import java.util.function.Function;

@FunctionalInterface
public interface Function7<A, B, C, D, E, F, G, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f, G g);
	
	default <RES_MAPPED> Function7<A, B, C, D, E, F, G, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f, g) -> mapper.apply(apply(a, b, c, d, e, f, g));
	}
}