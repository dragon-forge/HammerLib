package org.zeith.hammerlib.util.java.consumers;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface Consumer1<A>
		extends Consumer<A>
{
	@Override
	void accept(A a);
	
	default Consumer1<A> andThen(Consumer1<A> after)
	{
		Objects.requireNonNull(after);
		return (a) ->
		{
			accept(a);
			after.accept(a);
		};
	}
	
	@Nonnull
	@Override
	default Consumer1<A> andThen(@Nonnull Consumer<? super A> after)
	{
		return andThen(after::accept);
	}
}