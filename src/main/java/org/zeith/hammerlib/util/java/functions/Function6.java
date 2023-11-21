package org.zeith.hammerlib.util.java.functions;

import org.zeith.hammerlib.util.java.consumers.Consumer6;

import java.util.function.Function;

@FunctionalInterface
public interface Function6<A, B, C, D, E, F, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f);

	default <RES_MAPPED> Function6<A, B, C, D, E, F, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f) -> mapper.apply(apply(a, b, c, d, e, f));
	}
	
	default Consumer6<A, B, C, D, E, F> ignoreResult()
	{
		return this::apply;
	}
	
	default Function5<B, C, D, E, F, RES> constL(A a)
	{
		return (b, c, d, e, f) -> apply(a, b, c, d, e, f);
	}
	
	default Function4<C, D, E, F, RES> constL(A a, B b)
	{
		return (c, d, e, f) -> apply(a, b, c, d, e, f);
	}
	
	default Function3<D, E, F, RES> constL(A a, B b, C c)
	{
		return (d, e, f) -> apply(a, b, c, d, e, f);
	}
	
	default Function2<E, F, RES> constL(A a, B b, C c, D d)
	{
		return (e, f) -> apply(a, b, c, d, e, f);
	}
	
	default Function1<F, RES> constL(A a, B b, C c, D d, E e)
	{
		return (f) -> apply(a, b, c, d, e, f);
	}
	
	default Function5<A, B, C, D, E, RES> constR(F f)
	{
		return (a, b, c, d, e) -> apply(a, b, c, d, e, f);
	}
	
	default Function4<A, B, C, D, RES> constR(E e, F f)
	{
		return (a, b, c, d) -> apply(a, b, c, d, e, f);
	}
	
	default Function3<A, B, C, RES> constR(D d, E e, F f)
	{
		return (a, b, c) -> apply(a, b, c, d, e, f);
	}
	
	default Function2<A, B, RES> constR(C c, D d, E e, F f)
	{
		return (a, b) -> apply(a, b, c, d, e, f);
	}
	
	default Function1<A, RES> constR(B b, C c, D d, E e, F f)
	{
		return (a) -> apply(a, b, c, d, e, f);
	}
}