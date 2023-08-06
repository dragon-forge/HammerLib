package com.zeitheron.hammercore.utils.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer9<A, B, C, D, E, F, G, H, I>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i);
	
	default Consumer9<A, B, C, D, E, F, G, H, I> andThen(Consumer9<A, B, C, D, E, F, G, H, I> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h, i) ->
		{
			accept(a, b, c, d, e, f, g, h, i);
			after.accept(a, b, c, d, e, f, g, h, i);
		};
	}
}