package org.zeith.hammerlib.util.java.consumers;


import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.Objects;

@FunctionalInterface
public interface Consumer1<A>
		extends Consumer<A>
{
	@Override
	void accept(A a);

	default Consumer1<A> andThen(Consumer1<? super A> after)
	{
		Objects.requireNonNull(after);
		return (a) ->
		{
			accept(a);
			after.accept(a);
		};
	}
	
	@NotNull
	@Override
	default Consumer1<A> andThen(@NotNull Consumer<? super A> after)
	{
		return andThen(after::accept);
	}
}