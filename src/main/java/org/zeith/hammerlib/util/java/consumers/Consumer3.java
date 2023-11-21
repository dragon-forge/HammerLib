package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer3<A, B, C>
{
	void accept(A a, B b, C c);
	
	default Consumer3<A, B, C> andThen(Consumer3<? super A, ? super B, ? super C> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c) ->
		{
			accept(a, b, c);
			after.accept(a, b, c);
		};
	}
	
	default Consumer2<B, C> constL(A a)
	{
		return (b, c) -> accept(a, b, c);
	}
	
	default Consumer1<C> constL(A a, B b)
	{
		return (c) -> accept(a, b, c);
	}
	
	default Consumer2<A, B> constR(C c)
	{
		return (a, b) -> accept(a, b, c);
	}
	
	default Consumer1<A> constR(B b, C c)
	{
		return (a) -> accept(a, b, c);
	}
}