package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o);
	
	default Consumer15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> andThen(Consumer15<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L, ? super M, ? super N, ? super O> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) ->
		{
			accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
			after.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
		};
	}
	
	default Consumer14<B, C, D, E, F, G, H, I, J, K, L, M, N, O> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer13<C, D, E, F, G, H, I, J, K, L, M, N, O> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k, l, m, n, o) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer12<D, E, F, G, H, I, J, K, L, M, N, O> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k, l, m, n, o) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer11<E, F, G, H, I, J, K, L, M, N, O> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k, l, m, n, o) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer10<F, G, H, I, J, K, L, M, N, O> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k, l, m, n, o) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer9<G, H, I, J, K, L, M, N, O> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k, l, m, n, o) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer8<H, I, J, K, L, M, N, O> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k, l, m, n, o) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer7<I, J, K, L, M, N, O> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k, l, m, n, o) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer6<J, K, L, M, N, O> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k, l, m, n, o) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer5<K, L, M, N, O> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k, l, m, n, o) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer4<L, M, N, O> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (l, m, n, o) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer3<M, N, O> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (m, n, o) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer2<N, O> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (n, o) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer1<O> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (o) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> constR(O o)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer13<A, B, C, D, E, F, G, H, I, J, K, L, M> constR(N n, O o)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer12<A, B, C, D, E, F, G, H, I, J, K, L> constR(M m, N n, O o)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer11<A, B, C, D, E, F, G, H, I, J, K> constR(L l, M m, N n, O o)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer10<A, B, C, D, E, F, G, H, I, J> constR(K k, L l, M m, N n, O o)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer9<A, B, C, D, E, F, G, H, I> constR(J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c, d, e, f, g, h, i) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer8<A, B, C, D, E, F, G, H> constR(I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c, d, e, f, g, h) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer7<A, B, C, D, E, F, G> constR(H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c, d, e, f, g) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer6<A, B, C, D, E, F> constR(G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c, d, e, f) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer5<A, B, C, D, E> constR(F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c, d, e) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer4<A, B, C, D> constR(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c, d) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer3<A, B, C> constR(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer2<A, B> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Consumer1<A> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
}