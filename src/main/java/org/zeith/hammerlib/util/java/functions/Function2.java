package org.zeith.hammerlib.util.java.functions;

import org.zeith.hammerlib.util.java.consumers.Consumer2;

import java.util.function.*;

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
	
	default Consumer2<A, B> ignoreResult()
	{
		return this::apply;
	}
	
	default Function1<B, RES> constL(A a)
	{
		return (b) -> apply(a, b);
	}
	
	default Function1<A, RES> constR(B b)
	{
		return (a) -> apply(a, b);
	}
}