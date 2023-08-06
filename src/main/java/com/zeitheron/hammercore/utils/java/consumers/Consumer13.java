package com.zeitheron.hammercore.utils.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer13<A, B, C, D, E, F, G, H, I, J, K, L, M>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m);
	
	default Consumer13<A, B, C, D, E, F, G, H, I, J, K, L, M> andThen(Consumer13<A, B, C, D, E, F, G, H, I, J, K, L, M> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) ->
		{
			accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
			after.accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
		};
	}
}