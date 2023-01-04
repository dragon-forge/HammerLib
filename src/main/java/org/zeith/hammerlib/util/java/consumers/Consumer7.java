package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer7<A, B, C, D, E, F, G>
{
	void accept(A a, B b, C c, D d, E e, F f, G g);
	
	default Consumer7<A, B, C, D, E, F, G> andThen(Consumer7<A, B, C, D, E, F, G> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g) ->
		{
			accept(a, b, c, d, e, f, g);
			after.accept(a, b, c, d, e, f, g);
		};
	}
}