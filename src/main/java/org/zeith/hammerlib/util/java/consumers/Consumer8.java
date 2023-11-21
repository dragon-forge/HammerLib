package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer8<A, B, C, D, E, F, G, H>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h);
	
	default Consumer8<A, B, C, D, E, F, G, H> andThen(Consumer8<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h) ->
		{
			accept(a, b, c, d, e, f, g, h);
			after.accept(a, b, c, d, e, f, g, h);
		};
	}
	
	default Consumer7<B, C, D, E, F, G, H> constL(A a)
	{
		return (b, c, d, e, f, g, h) -> accept(a, b, c, d, e, f, g, h);
	}
	
	default Consumer6<C, D, E, F, G, H> constL(A a, B b)
	{
		return (c, d, e, f, g, h) -> accept(a, b, c, d, e, f, g, h);
	}
	
	default Consumer5<D, E, F, G, H> constL(A a, B b, C c)
	{
		return (d, e, f, g, h) -> accept(a, b, c, d, e, f, g, h);
	}
	
	default Consumer4<E, F, G, H> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h) -> accept(a, b, c, d, e, f, g, h);
	}
	
	default Consumer3<F, G, H> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h) -> accept(a, b, c, d, e, f, g, h);
	}
	
	default Consumer2<G, H> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h) -> accept(a, b, c, d, e, f, g, h);
	}
	
	default Consumer1<H> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h) -> accept(a, b, c, d, e, f, g, h);
	}
	
	default Consumer7<A, B, C, D, E, F, G> constR(H h)
	{
		return (a, b, c, d, e, f, g) -> accept(a, b, c, d, e, f, g, h);
	}
	
	default Consumer6<A, B, C, D, E, F> constR(G g, H h)
	{
		return (a, b, c, d, e, f) -> accept(a, b, c, d, e, f, g, h);
	}
	
	default Consumer5<A, B, C, D, E> constR(F f, G g, H h)
	{
		return (a, b, c, d, e) -> accept(a, b, c, d, e, f, g, h);
	}
	
	default Consumer4<A, B, C, D> constR(E e, F f, G g, H h)
	{
		return (a, b, c, d) -> accept(a, b, c, d, e, f, g, h);
	}
	
	default Consumer3<A, B, C> constR(D d, E e, F f, G g, H h)
	{
		return (a, b, c) -> accept(a, b, c, d, e, f, g, h);
	}
	
	default Consumer2<A, B> constR(C c, D d, E e, F f, G g, H h)
	{
		return (a, b) -> accept(a, b, c, d, e, f, g, h);
	}
	
	default Consumer1<A> constR(B b, C c, D d, E e, F f, G g, H h)
	{
		return (a) -> accept(a, b, c, d, e, f, g, h);
	}
}