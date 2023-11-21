package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer5<A, B, C, D, E>
{
	void accept(A a, B b, C c, D d, E e);

	default Consumer5<A, B, C, D, E> andThen(Consumer5<? super A, ? super B, ? super C, ? super D, ? super E> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e) ->
		{
			accept(a, b, c, d, e);
			after.accept(a, b, c, d, e);
		};
	}

	default Consumer4<B, C, D, E> constL(A a)
	{
		return (b, c, d, e) -> accept(a, b, c, d, e);
	}
	
	default Consumer3<C, D, E> constL(A a, B b)
	{
		return (c, d, e) -> accept(a, b, c, d, e);
	}
	
	default Consumer2<D, E> constL(A a, B b, C c)
	{
		return (d, e) -> accept(a, b, c, d, e);
	}
	
	default Consumer1<E> constL(A a, B b, C c, D d)
	{
		return (e) -> accept(a, b, c, d, e);
	}

	default Consumer4<A, B, C, D> constR(E e)
	{
		return (a, b, c, d) -> accept(a, b, c, d, e);
	}
	
	default Consumer3<A, B, C> constR(D d, E e)
	{
		return (a, b, c) -> accept(a, b, c, d, e);
	}
	
	default Consumer2<A, B> constR(C c, D d, E e)
	{
		return (a, b) -> accept(a, b, c, d, e);
	}
	
	default Consumer1<A> constR(B b, C c, D d, E e)
	{
		return (a) -> accept(a, b, c, d, e);
	}
}