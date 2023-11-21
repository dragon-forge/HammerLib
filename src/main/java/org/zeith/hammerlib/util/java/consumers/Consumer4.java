package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer4<A, B, C, D>
{
	void accept(A a, B b, C c, D d);

	default Consumer4<A, B, C, D> andThen(Consumer4<? super A, ? super B, ? super C, ? super D> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d) ->
		{
			accept(a, b, c, d);
			after.accept(a, b, c, d);
		};
	}

	default Consumer3<B, C, D> constL(A a)
	{
		return (b, c, d) -> accept(a, b, c, d);
	}
	
	default Consumer2<C, D> constL(A a, B b)
	{
		return (c, d) -> accept(a, b, c, d);
	}
	
	default Consumer1<D> constL(A a, B b, C c)
	{
		return (d) -> accept(a, b, c, d);
	}

	default Consumer3<A, B, C> constR(D d)
	{
		return (a, b, c) -> accept(a, b, c, d);
	}
	
	default Consumer2<A, B> constR(C c, D d)
	{
		return (a, b) -> accept(a, b, c, d);
	}
	
	default Consumer1<A> constR(B b, C c, D d)
	{
		return (a) -> accept(a, b, c, d);
	}
}