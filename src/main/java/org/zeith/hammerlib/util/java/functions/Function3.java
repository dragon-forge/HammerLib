package org.zeith.hammerlib.util.java.functions;

import org.zeith.hammerlib.util.java.consumers.Consumer3;

import java.util.function.Function;

@FunctionalInterface
public interface Function3<A, B, C, RES>
{
	RES apply(A a, B b, C c);

	default <RES_MAPPED> Function3<A, B, C, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c) -> mapper.apply(apply(a, b, c));
	}
	
	default Consumer3<A, B, C> ignoreResult()
	{
		return this::apply;
	}
	
	default Function2<B, C, RES> constL(A a)
	{
		return (b, c) -> apply(a, b, c);
	}
	
	default Function1<C, RES> constL(A a, B b)
	{
		return (c) -> apply(a, b, c);
	}
	
	default Function2<A, B, RES> constR(C c)
	{
		return (a, b) -> apply(a, b, c);
	}
	
	default Function1<A, RES> constR(B b, C c)
	{
		return (a) -> apply(a, b, c);
	}
}