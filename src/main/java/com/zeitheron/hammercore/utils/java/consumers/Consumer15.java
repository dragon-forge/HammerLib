package com.zeitheron.hammercore.utils.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o);
	
	default Consumer15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> andThen(Consumer15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) ->
		{
			accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
			after.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
		};
	}
}