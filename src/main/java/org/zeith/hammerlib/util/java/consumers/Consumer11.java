package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer11<A, B, C, D, E, F, G, H, I, J, K>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k);

	default Consumer11<A, B, C, D, E, F, G, H, I, J, K> andThen(Consumer11<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h, i, j, k) ->
		{
			accept(a, b, c, d, e, f, g, h, i, j, k);
			after.accept(a, b, c, d, e, f, g, h, i, j, k);
		};
	}

	default Consumer10<B, C, D, E, F, G, H, I, J, K> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Consumer9<C, D, E, F, G, H, I, J, K> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Consumer8<D, E, F, G, H, I, J, K> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Consumer7<E, F, G, H, I, J, K> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Consumer6<F, G, H, I, J, K> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Consumer5<G, H, I, J, K> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Consumer4<H, I, J, K> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Consumer3<I, J, K> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Consumer2<J, K> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Consumer1<K> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}

	default Consumer10<A, B, C, D, E, F, G, H, I, J> constR(K k)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Consumer9<A, B, C, D, E, F, G, H, I> constR(J j, K k)
	{
		return (a, b, c, d, e, f, g, h, i) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Consumer8<A, B, C, D, E, F, G, H> constR(I i, J j, K k)
	{
		return (a, b, c, d, e, f, g, h) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Consumer7<A, B, C, D, E, F, G> constR(H h, I i, J j, K k)
	{
		return (a, b, c, d, e, f, g) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Consumer6<A, B, C, D, E, F> constR(G g, H h, I i, J j, K k)
	{
		return (a, b, c, d, e, f) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Consumer5<A, B, C, D, E> constR(F f, G g, H h, I i, J j, K k)
	{
		return (a, b, c, d, e) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Consumer4<A, B, C, D> constR(E e, F f, G g, H h, I i, J j, K k)
	{
		return (a, b, c, d) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Consumer3<A, B, C> constR(D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (a, b, c) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Consumer2<A, B> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (a, b) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Consumer1<A> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (a) -> accept(a, b, c, d, e, f, g, h, i, j, k);
	}
}