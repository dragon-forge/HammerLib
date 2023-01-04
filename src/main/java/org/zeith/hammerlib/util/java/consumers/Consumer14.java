package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n);
	
	default Consumer14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> andThen(Consumer14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n) ->
		{
			accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
			after.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
		};
	}
}