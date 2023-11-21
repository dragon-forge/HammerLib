package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer9<A, B, C, D, E, F, G, H, I>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i);
	
	default Consumer9<A, B, C, D, E, F, G, H, I> andThen(Consumer9<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h, i) ->
		{
			accept(a, b, c, d, e, f, g, h, i);
			after.accept(a, b, c, d, e, f, g, h, i);
		};
	}
	
	default Consumer8<B, C, D, E, F, G, H, I> constL(A a)
	{
		return (b, c, d, e, f, g, h, i) -> accept(a, b, c, d, e, f, g, h, i);
	}
	
	default Consumer7<C, D, E, F, G, H, I> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i) -> accept(a, b, c, d, e, f, g, h, i);
	}
	
	default Consumer6<D, E, F, G, H, I> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i) -> accept(a, b, c, d, e, f, g, h, i);
	}
	
	default Consumer5<E, F, G, H, I> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i) -> accept(a, b, c, d, e, f, g, h, i);
	}
	
	default Consumer4<F, G, H, I> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i) -> accept(a, b, c, d, e, f, g, h, i);
	}
	
	default Consumer3<G, H, I> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i) -> accept(a, b, c, d, e, f, g, h, i);
	}
	
	default Consumer2<H, I> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i) -> accept(a, b, c, d, e, f, g, h, i);
	}
	
	default Consumer1<I> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i) -> accept(a, b, c, d, e, f, g, h, i);
	}
	
	default Consumer8<A, B, C, D, E, F, G, H> constR(I i)
	{
		return (a, b, c, d, e, f, g, h) -> accept(a, b, c, d, e, f, g, h, i);
	}
	
	default Consumer7<A, B, C, D, E, F, G> constR(H h, I i)
	{
		return (a, b, c, d, e, f, g) -> accept(a, b, c, d, e, f, g, h, i);
	}
	
	default Consumer6<A, B, C, D, E, F> constR(G g, H h, I i)
	{
		return (a, b, c, d, e, f) -> accept(a, b, c, d, e, f, g, h, i);
	}
	
	default Consumer5<A, B, C, D, E> constR(F f, G g, H h, I i)
	{
		return (a, b, c, d, e) -> accept(a, b, c, d, e, f, g, h, i);
	}
	
	default Consumer4<A, B, C, D> constR(E e, F f, G g, H h, I i)
	{
		return (a, b, c, d) -> accept(a, b, c, d, e, f, g, h, i);
	}
	
	default Consumer3<A, B, C> constR(D d, E e, F f, G g, H h, I i)
	{
		return (a, b, c) -> accept(a, b, c, d, e, f, g, h, i);
	}
	
	default Consumer2<A, B> constR(C c, D d, E e, F f, G g, H h, I i)
	{
		return (a, b) -> accept(a, b, c, d, e, f, g, h, i);
	}
	
	default Consumer1<A> constR(B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (a) -> accept(a, b, c, d, e, f, g, h, i);
	}
}