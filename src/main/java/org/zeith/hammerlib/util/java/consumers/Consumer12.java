package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer12<A, B, C, D, E, F, G, H, I, J, K, L>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l);

	default Consumer12<A, B, C, D, E, F, G, H, I, J, K, L> andThen(Consumer12<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h, i, j, k, l) ->
		{
			accept(a, b, c, d, e, f, g, h, i, j, k, l);
			after.accept(a, b, c, d, e, f, g, h, i, j, k, l);
		};
	}

	default Consumer11<B, C, D, E, F, G, H, I, J, K, L> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k, l) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer10<C, D, E, F, G, H, I, J, K, L> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k, l) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer9<D, E, F, G, H, I, J, K, L> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k, l) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer8<E, F, G, H, I, J, K, L> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k, l) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer7<F, G, H, I, J, K, L> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k, l) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer6<G, H, I, J, K, L> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k, l) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer5<H, I, J, K, L> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k, l) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer4<I, J, K, L> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k, l) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer3<J, K, L> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k, l) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer2<K, L> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k, l) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer1<L> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (l) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}

	default Consumer11<A, B, C, D, E, F, G, H, I, J, K> constR(L l)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer10<A, B, C, D, E, F, G, H, I, J> constR(K k, L l)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer9<A, B, C, D, E, F, G, H, I> constR(J j, K k, L l)
	{
		return (a, b, c, d, e, f, g, h, i) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer8<A, B, C, D, E, F, G, H> constR(I i, J j, K k, L l)
	{
		return (a, b, c, d, e, f, g, h) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer7<A, B, C, D, E, F, G> constR(H h, I i, J j, K k, L l)
	{
		return (a, b, c, d, e, f, g) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer6<A, B, C, D, E, F> constR(G g, H h, I i, J j, K k, L l)
	{
		return (a, b, c, d, e, f) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer5<A, B, C, D, E> constR(F f, G g, H h, I i, J j, K k, L l)
	{
		return (a, b, c, d, e) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer4<A, B, C, D> constR(E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (a, b, c, d) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer3<A, B, C> constR(D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (a, b, c) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer2<A, B> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (a, b) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Consumer1<A> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (a) -> accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
}