package org.zeith.hammerlib.util.java.itf;

import java.util.Objects;

@FunctionalInterface
public interface BooleanConsumer
{
	void accept(boolean value);
	
	default BooleanConsumer andThen(BooleanConsumer after)
	{
		Objects.requireNonNull(after);
		return (boolean t) ->
		{
			accept(t);
			after.accept(t);
		};
	}
}
