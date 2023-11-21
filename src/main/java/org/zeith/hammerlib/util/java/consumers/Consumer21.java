package org.zeith.hammerlib.util.java.consumers;

import java.util.Objects;

@FunctionalInterface
public interface Consumer21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U>
{
	void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u);

	default Consumer21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> andThen(Consumer21<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L, ? super M, ? super N, ? super O, ? super P, ? super Q, ? super R, ? super S, ? super T, ? super U> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u) ->
		{
			accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
			after.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
		};
	}

	default Consumer20<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer19<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer18<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer17<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer16<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer15<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k, l, m, n, o, p, q, r, s, t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer14<H, I, J, K, L, M, N, O, P, Q, R, S, T, U> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k, l, m, n, o, p, q, r, s, t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer13<I, J, K, L, M, N, O, P, Q, R, S, T, U> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k, l, m, n, o, p, q, r, s, t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer12<J, K, L, M, N, O, P, Q, R, S, T, U> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k, l, m, n, o, p, q, r, s, t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer11<K, L, M, N, O, P, Q, R, S, T, U> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k, l, m, n, o, p, q, r, s, t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer10<L, M, N, O, P, Q, R, S, T, U> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (l, m, n, o, p, q, r, s, t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer9<M, N, O, P, Q, R, S, T, U> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (m, n, o, p, q, r, s, t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer8<N, O, P, Q, R, S, T, U> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (n, o, p, q, r, s, t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer7<O, P, Q, R, S, T, U> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (o, p, q, r, s, t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer6<P, Q, R, S, T, U> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (p, q, r, s, t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer5<Q, R, S, T, U> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return (q, r, s, t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer4<R, S, T, U> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return (r, s, t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer3<S, T, U> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
	{
		return (s, t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer2<T, U> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
	{
		return (t, u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer1<U> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return (u) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}

	default Consumer20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> constR(U u)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> constR(T t, U u)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> constR(S s, T t, U u)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> constR(R r, S s, T t, U u)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> constR(Q q, R r, S s, T t, U u)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> constR(P p, Q q, R r, S s, T t, U u)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> constR(O o, P p, Q q, R r, S s, T t, U u)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer13<A, B, C, D, E, F, G, H, I, J, K, L, M> constR(N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer12<A, B, C, D, E, F, G, H, I, J, K, L> constR(M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer11<A, B, C, D, E, F, G, H, I, J, K> constR(L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer10<A, B, C, D, E, F, G, H, I, J> constR(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer9<A, B, C, D, E, F, G, H, I> constR(J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return (a, b, c, d, e, f, g, h, i) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer8<A, B, C, D, E, F, G, H> constR(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return (a, b, c, d, e, f, g, h) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer7<A, B, C, D, E, F, G> constR(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return (a, b, c, d, e, f, g) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer6<A, B, C, D, E, F> constR(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return (a, b, c, d, e, f) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer5<A, B, C, D, E> constR(F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return (a, b, c, d, e) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer4<A, B, C, D> constR(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return (a, b, c, d) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer3<A, B, C> constR(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return (a, b, c) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer2<A, B> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return (a, b) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	default Consumer1<A> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return (a) -> accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
}