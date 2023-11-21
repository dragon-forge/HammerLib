package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer13<A, B, C, D, E, F, G, H, I, J, K, L, M>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m);
	
	default Consumer13<A, B, C, D, E, F, G, H, I, J, K, L, M> andThen(Consumer13<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L, ? super M> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) ->
		{
			accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
			after.accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
		};
	}
	
	default Consumer12<B, C, D, E, F, G, H, I, J, K, L, M> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k, l, m) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer11<C, D, E, F, G, H, I, J, K, L, M> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k, l, m) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer10<D, E, F, G, H, I, J, K, L, M> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k, l, m) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer9<E, F, G, H, I, J, K, L, M> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k, l, m) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer8<F, G, H, I, J, K, L, M> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k, l, m) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer7<G, H, I, J, K, L, M> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k, l, m) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer6<H, I, J, K, L, M> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k, l, m) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer5<I, J, K, L, M> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k, l, m) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer4<J, K, L, M> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k, l, m) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer3<K, L, M> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k, l, m) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer2<L, M> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (l, m) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer1<M> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (m) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer12<A, B, C, D, E, F, G, H, I, J, K, L> constR(M m)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer11<A, B, C, D, E, F, G, H, I, J, K> constR(L l, M m)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer10<A, B, C, D, E, F, G, H, I, J> constR(K k, L l, M m)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer9<A, B, C, D, E, F, G, H, I> constR(J j, K k, L l, M m)
	{
		return (a, b, c, d, e, f, g, h, i) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer8<A, B, C, D, E, F, G, H> constR(I i, J j, K k, L l, M m)
	{
		return (a, b, c, d, e, f, g, h) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer7<A, B, C, D, E, F, G> constR(H h, I i, J j, K k, L l, M m)
	{
		return (a, b, c, d, e, f, g) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer6<A, B, C, D, E, F> constR(G g, H h, I i, J j, K k, L l, M m)
	{
		return (a, b, c, d, e, f) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer5<A, B, C, D, E> constR(F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (a, b, c, d, e) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer4<A, B, C, D> constR(E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (a, b, c, d) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer3<A, B, C> constR(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (a, b, c) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer2<A, B> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (a, b) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Consumer1<A> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (a) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
}