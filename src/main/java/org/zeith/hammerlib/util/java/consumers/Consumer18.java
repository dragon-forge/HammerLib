package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r);
	
	default Consumer18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> andThen(Consumer18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r) ->
		{
			accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
			after.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
		};
	}
}