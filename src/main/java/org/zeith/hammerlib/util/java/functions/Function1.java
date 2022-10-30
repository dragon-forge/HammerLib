package org.zeith.hammerlib.util.java.functions;

import java.util.function.Function;

@FunctionalInterface
public interface Function1<A, RES>
		extends Function<A, RES>
{
	@Override
	RES apply(A a);
	
	default <RES_MAPPED> Function1<A, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a) -> mapper.apply(apply(a));
	}
}