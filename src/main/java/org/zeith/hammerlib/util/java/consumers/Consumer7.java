package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer7<A, B, C, D, E, F, G>
{
	void accept(A a, B b, C c, D d, E e, F f, G g);
	
	default Consumer7<A, B, C, D, E, F, G> andThen(Consumer7<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g) ->
		{
			accept(a, b, c, d, e, f, g);
			after.accept(a, b, c, d, e, f, g);
		};
	}
	
	default Consumer6<B, C, D, E, F, G> constL(A a)
	{
		return (b, c, d, e, f, g) -> accept(a, b, c, d, e, f, g);
	}
	
	default Consumer5<C, D, E, F, G> constL(A a, B b)
	{
		return (c, d, e, f, g) -> accept(a, b, c, d, e, f, g);
	}
	
	default Consumer4<D, E, F, G> constL(A a, B b, C c)
	{
		return (d, e, f, g) -> accept(a, b, c, d, e, f, g);
	}
	
	default Consumer3<E, F, G> constL(A a, B b, C c, D d)
	{
		return (e, f, g) -> accept(a, b, c, d, e, f, g);
	}
	
	default Consumer2<F, G> constL(A a, B b, C c, D d, E e)
	{
		return (f, g) -> accept(a, b, c, d, e, f, g);
	}
	
	default Consumer1<G> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g) -> accept(a, b, c, d, e, f, g);
	}
	
	default Consumer6<A, B, C, D, E, F> constR(G g)
	{
		return (a, b, c, d, e, f) -> accept(a, b, c, d, e, f, g);
	}
	
	default Consumer5<A, B, C, D, E> constR(F f, G g)
	{
		return (a, b, c, d, e) -> accept(a, b, c, d, e, f, g);
	}
	
	default Consumer4<A, B, C, D> constR(E e, F f, G g)
	{
		return (a, b, c, d) -> accept(a, b, c, d, e, f, g);
	}
	
	default Consumer3<A, B, C> constR(D d, E e, F f, G g)
	{
		return (a, b, c) -> accept(a, b, c, d, e, f, g);
	}
	
	default Consumer2<A, B> constR(C c, D d, E e, F f, G g)
	{
		return (a, b) -> accept(a, b, c, d, e, f, g);
	}
	
	default Consumer1<A> constR(B b, C c, D d, E e, F f, G g)
	{
		return (a) -> accept(a, b, c, d, e, f, g);
	}
}