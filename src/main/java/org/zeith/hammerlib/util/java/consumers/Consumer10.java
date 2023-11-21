package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer10<A, B, C, D, E, F, G, H, I, J>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j);

	default Consumer10<A, B, C, D, E, F, G, H, I, J> andThen(Consumer10<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h, i, j) ->
		{
			accept(a, b, c, d, e, f, g, h, i, j);
			after.accept(a, b, c, d, e, f, g, h, i, j);
		};
	}

	default Consumer9<B, C, D, E, F, G, H, I, J> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j) -> accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Consumer8<C, D, E, F, G, H, I, J> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j) -> accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Consumer7<D, E, F, G, H, I, J> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j) -> accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Consumer6<E, F, G, H, I, J> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j) -> accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Consumer5<F, G, H, I, J> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j) -> accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Consumer4<G, H, I, J> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j) -> accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Consumer3<H, I, J> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j) -> accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Consumer2<I, J> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j) -> accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Consumer1<J> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j) -> accept(a, b, c, d, e, f, g, h, i, j);
	}

	default Consumer9<A, B, C, D, E, F, G, H, I> constR(J j)
	{
		return (a, b, c, d, e, f, g, h, i) -> accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Consumer8<A, B, C, D, E, F, G, H> constR(I i, J j)
	{
		return (a, b, c, d, e, f, g, h) -> accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Consumer7<A, B, C, D, E, F, G> constR(H h, I i, J j)
	{
		return (a, b, c, d, e, f, g) -> accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Consumer6<A, B, C, D, E, F> constR(G g, H h, I i, J j)
	{
		return (a, b, c, d, e, f) -> accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Consumer5<A, B, C, D, E> constR(F f, G g, H h, I i, J j)
	{
		return (a, b, c, d, e) -> accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Consumer4<A, B, C, D> constR(E e, F f, G g, H h, I i, J j)
	{
		return (a, b, c, d) -> accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Consumer3<A, B, C> constR(D d, E e, F f, G g, H h, I i, J j)
	{
		return (a, b, c) -> accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Consumer2<A, B> constR(C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (a, b) -> accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Consumer1<A> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (a) -> accept(a, b, c, d, e, f, g, h, i, j);
	}
}