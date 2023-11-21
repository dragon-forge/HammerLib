package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer6<A, B, C, D, E, F>
{
	void accept(A a, B b, C c, D d, E e, F f);
	
	default Consumer6<A, B, C, D, E, F> andThen(Consumer6<? super A, ? super B, ? super C, ? super D, ? super E, ? super F> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f) ->
		{
			accept(a, b, c, d, e, f);
			after.accept(a, b, c, d, e, f);
		};
	}
	
	default Consumer5<B, C, D, E, F> constL(A a)
	{
		return (b, c, d, e, f) -> accept(a, b, c, d, e, f);
	}
	
	default Consumer4<C, D, E, F> constL(A a, B b)
	{
		return (c, d, e, f) -> accept(a, b, c, d, e, f);
	}
	
	default Consumer3<D, E, F> constL(A a, B b, C c)
	{
		return (d, e, f) -> accept(a, b, c, d, e, f);
	}
	
	default Consumer2<E, F> constL(A a, B b, C c, D d)
	{
		return (e, f) -> accept(a, b, c, d, e, f);
	}
	
	default Consumer1<F> constL(A a, B b, C c, D d, E e)
	{
		return (f) -> accept(a, b, c, d, e, f);
	}
	
	default Consumer5<A, B, C, D, E> constR(F f)
	{
		return (a, b, c, d, e) -> accept(a, b, c, d, e, f);
	}
	
	default Consumer4<A, B, C, D> constR(E e, F f)
	{
		return (a, b, c, d) -> accept(a, b, c, d, e, f);
	}
	
	default Consumer3<A, B, C> constR(D d, E e, F f)
	{
		return (a, b, c) -> accept(a, b, c, d, e, f);
	}
	
	default Consumer2<A, B> constR(C c, D d, E e, F f)
	{
		return (a, b) -> accept(a, b, c, d, e, f);
	}
	
	default Consumer1<A> constR(B b, C c, D d, E e, F f)
	{
		return (a) -> accept(a, b, c, d, e, f);
	}
}