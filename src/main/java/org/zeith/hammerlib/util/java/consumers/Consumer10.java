package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer10<A, B, C, D, E, F, G, H, I, J>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j);
	
	default Consumer10<A, B, C, D, E, F, G, H, I, J> andThen(Consumer10<A, B, C, D, E, F, G, H, I, J> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h, i, j) ->
		{
			accept(a, b, c, d, e, f, g, h, i, j);
			after.accept(a, b, c, d, e, f, g, h, i, j);
		};
	}
}