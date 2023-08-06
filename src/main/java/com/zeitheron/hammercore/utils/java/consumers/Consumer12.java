package com.zeitheron.hammercore.utils.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer12<A, B, C, D, E, F, G, H, I, J, K, L>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l);
	
	default Consumer12<A, B, C, D, E, F, G, H, I, J, K, L> andThen(Consumer12<A, B, C, D, E, F, G, H, I, J, K, L> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h, i, j, k, l) ->
		{
			accept(a, b, c, d, e, f, g, h, i, j, k, l);
			after.accept(a, b, c, d, e, f, g, h, i, j, k, l);
		};
	}
}