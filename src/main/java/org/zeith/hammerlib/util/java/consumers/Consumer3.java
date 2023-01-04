package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer3<A, B, C>
{
	void accept(A a, B b, C c);
	
	default Consumer3<A, B, C> andThen(Consumer3<A, B, C> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c) ->
		{
			accept(a, b, c);
			after.accept(a, b, c);
		};
	}
}