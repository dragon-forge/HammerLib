package com.zeitheron.hammercore.utils.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer5<A, B, C, D, E>
{
	void accept(A a, B b, C c, D d, E e);
	
	default Consumer5<A, B, C, D, E> andThen(Consumer5<A, B, C, D, E> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e) ->
		{
			accept(a, b, c, d, e);
			after.accept(a, b, c, d, e);
		};
	}
}