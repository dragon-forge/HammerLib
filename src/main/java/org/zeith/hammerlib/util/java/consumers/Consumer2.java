package org.zeith.hammerlib.util.java.consumers;


import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface Consumer2<A, B>
		extends BiConsumer<A, B>
{
	@Override
	void accept(A a, B b);
	
	default Consumer2<A, B> andThen(Consumer2<? super A, ? super B> after)
	{
		Objects.requireNonNull(after);
		return (a, b) ->
		{
			accept(a, b);
			after.accept(a, b);
		};
	}
	
	@NotNull
	@Override
	default Consumer2<A, B> andThen(@NotNull BiConsumer<? super A, ? super B> after)
	{
		return andThen(after::accept);
	}
	
	default Consumer1<B> constL(A a)
	{
		return (b) -> accept(a, b);
	}
	
	default Consumer1<A> constR(B b)
	{
		return (a) -> accept(a, b);
	}
}