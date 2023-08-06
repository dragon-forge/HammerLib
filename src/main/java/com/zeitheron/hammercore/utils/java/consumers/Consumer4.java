package com.zeitheron.hammercore.utils.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer4<A, B, C, D>
{
	void accept(A a, B b, C c, D d);
	
	default Consumer4<A, B, C, D> andThen(Consumer4<A, B, C, D> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d) ->
		{
			accept(a, b, c, d);
			after.accept(a, b, c, d);
		};
	}
}