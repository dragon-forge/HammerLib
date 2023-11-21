package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n);
	
	default Consumer14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> andThen(Consumer14<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L, ? super M, ? super N> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n) ->
		{
			accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
			after.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
		};
	}
	
	default Consumer13<B, C, D, E, F, G, H, I, J, K, L, M, N> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k, l, m, n) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer12<C, D, E, F, G, H, I, J, K, L, M, N> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k, l, m, n) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer11<D, E, F, G, H, I, J, K, L, M, N> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k, l, m, n) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer10<E, F, G, H, I, J, K, L, M, N> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k, l, m, n) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer9<F, G, H, I, J, K, L, M, N> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k, l, m, n) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer8<G, H, I, J, K, L, M, N> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k, l, m, n) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer7<H, I, J, K, L, M, N> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k, l, m, n) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer6<I, J, K, L, M, N> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k, l, m, n) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer5<J, K, L, M, N> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k, l, m, n) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer4<K, L, M, N> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k, l, m, n) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer3<L, M, N> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (l, m, n) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer2<M, N> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (m, n) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer1<N> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (n) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer13<A, B, C, D, E, F, G, H, I, J, K, L, M> constR(N n)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer12<A, B, C, D, E, F, G, H, I, J, K, L> constR(M m, N n)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer11<A, B, C, D, E, F, G, H, I, J, K> constR(L l, M m, N n)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer10<A, B, C, D, E, F, G, H, I, J> constR(K k, L l, M m, N n)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer9<A, B, C, D, E, F, G, H, I> constR(J j, K k, L l, M m, N n)
	{
		return (a, b, c, d, e, f, g, h, i) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer8<A, B, C, D, E, F, G, H> constR(I i, J j, K k, L l, M m, N n)
	{
		return (a, b, c, d, e, f, g, h) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer7<A, B, C, D, E, F, G> constR(H h, I i, J j, K k, L l, M m, N n)
	{
		return (a, b, c, d, e, f, g) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer6<A, B, C, D, E, F> constR(G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (a, b, c, d, e, f) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer5<A, B, C, D, E> constR(F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (a, b, c, d, e) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer4<A, B, C, D> constR(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (a, b, c, d) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer3<A, B, C> constR(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (a, b, c) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer2<A, B> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (a, b) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Consumer1<A> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (a) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
}