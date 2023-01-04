package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer2<A, B>
{
	void accept(A a, B b);
	
	default Consumer2<A, B> andThen(Consumer2<A, B> after)
	{
		Objects.requireNonNull(after);
		return (a, b) ->
		{
			accept(a, b);
			after.accept(a, b);
		};
	}
}