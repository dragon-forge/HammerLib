package com.zeitheron.hammercore.utils.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer8<A, B, C, D, E, F, G, H>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h);
	
	default Consumer8<A, B, C, D, E, F, G, H> andThen(Consumer8<A, B, C, D, E, F, G, H> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h) ->
		{
			accept(a, b, c, d, e, f, g, h);
			after.accept(a, b, c, d, e, f, g, h);
		};
	}
}