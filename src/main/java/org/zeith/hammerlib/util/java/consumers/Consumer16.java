package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p);
	
	default Consumer16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> andThen(Consumer16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) ->
		{
			accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
			after.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		};
	}
}