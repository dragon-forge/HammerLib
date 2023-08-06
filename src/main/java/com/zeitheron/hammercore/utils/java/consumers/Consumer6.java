package com.zeitheron.hammercore.utils.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer6<A, B, C, D, E, F>
{
	void accept(A a, B b, C c, D d, E e, F f);
	
	default Consumer6<A, B, C, D, E, F> andThen(Consumer6<A, B, C, D, E, F> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f) ->
		{
			accept(a, b, c, d, e, f);
			after.accept(a, b, c, d, e, f);
		};
	}
}