package org.zeith.hammerlib.util.java.functions;

import java.util.function.BiFunction;
import java.util.function.Function;

@FunctionalInterface
public interface Function2<A, B, RES>
		extends BiFunction<A, B, RES>
{
	@Override
	RES apply(A a, B b);
	
	default <RES_MAPPED> Function2<A, B, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b) -> mapper.apply(apply(a, b));
	}
}