package com.zeitheron.hammercore.utils.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer11<A, B, C, D, E, F, G, H, I, J, K>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k);
	
	default Consumer11<A, B, C, D, E, F, G, H, I, J, K> andThen(Consumer11<A, B, C, D, E, F, G, H, I, J, K> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h, i, j, k) ->
		{
			accept(a, b, c, d, e, f, g, h, i, j, k);
			after.accept(a, b, c, d, e, f, g, h, i, j, k);
		};
	}
}