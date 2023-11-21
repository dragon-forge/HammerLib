package org.zeith.hammerlib.util.java.functions;

import java.util.function.*;

import org.zeith.hammerlib.util.java.consumers.Consumer5;

@FunctionalInterface
public interface Function5<A, B, C, D, E, RES>
{
	RES apply(A a, B b, C c, D d, E e);

	default <RES_MAPPED> Function5<A, B, C, D, E, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e) -> mapper.apply(apply(a, b, c, d, e));
	}

	default Consumer5<A, B, C, D, E> ignoreResult()
	{
		return this::apply;
	}

	default Function4<B, C, D, E, RES> constL(A a)
	{
		return (b, c, d, e) -> apply(a, b, c, d, e);
	}
	
	default Function3<C, D, E, RES> constL(A a, B b)
	{
		return (c, d, e) -> apply(a, b, c, d, e);
	}
	
	default Function2<D, E, RES> constL(A a, B b, C c)
	{
		return (d, e) -> apply(a, b, c, d, e);
	}
	
	default Function1<E, RES> constL(A a, B b, C c, D d)
	{
		return (e) -> apply(a, b, c, d, e);
	}

	default Function4<A, B, C, D, RES> constR(E e)
	{
		return (a, b, c, d) -> apply(a, b, c, d, e);
	}
	
	default Function3<A, B, C, RES> constR(D d, E e)
	{
		return (a, b, c) -> apply(a, b, c, d, e);
	}
	
	default Function2<A, B, RES> constR(C c, D d, E e)
	{
		return (a, b) -> apply(a, b, c, d, e);
	}
	
	default Function1<A, RES> constR(B b, C c, D d, E e)
	{
		return (a) -> apply(a, b, c, d, e);
	}
}