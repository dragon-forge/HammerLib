package org.zeith.hammerlib.util.java.functions;

import java.util.function.*;

import org.zeith.hammerlib.util.java.consumers.Consumer4;

@FunctionalInterface
public interface Function4<A, B, C, D, RES>
{
	RES apply(A a, B b, C c, D d);

	default <RES_MAPPED> Function4<A, B, C, D, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d) -> mapper.apply(apply(a, b, c, d));
	}

	default Consumer4<A, B, C, D> ignoreResult()
	{
		return this::apply;
	}

	default Function3<B, C, D, RES> constL(A a)
	{
		return (b, c, d) -> apply(a, b, c, d);
	}
	
	default Function2<C, D, RES> constL(A a, B b)
	{
		return (c, d) -> apply(a, b, c, d);
	}
	
	default Function1<D, RES> constL(A a, B b, C c)
	{
		return (d) -> apply(a, b, c, d);
	}

	default Function3<A, B, C, RES> constR(D d)
	{
		return (a, b, c) -> apply(a, b, c, d);
	}
	
	default Function2<A, B, RES> constR(C c, D d)
	{
		return (a, b) -> apply(a, b, c, d);
	}
	
	default Function1<A, RES> constR(B b, C c, D d)
	{
		return (a) -> apply(a, b, c, d);
	}
}