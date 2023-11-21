package org.zeith.hammerlib.util.java.functions;

import org.zeith.hammerlib.util.java.consumers.Consumer23;

import java.util.function.Function;

@FunctionalInterface
public interface Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w);

	default <RES_MAPPED> Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w) -> mapper.apply(apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w));
	}
	
	default Consumer23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> ignoreResult()
	{
		return this::apply;
	}
	
	default Function22<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function21<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function20<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function19<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function18<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function17<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function16<H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function15<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k, l, m, n, o, p, q, r, s, t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function14<J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k, l, m, n, o, p, q, r, s, t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function13<K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k, l, m, n, o, p, q, r, s, t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function12<L, M, N, O, P, Q, R, S, T, U, V, W, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (l, m, n, o, p, q, r, s, t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function11<M, N, O, P, Q, R, S, T, U, V, W, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (m, n, o, p, q, r, s, t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function10<N, O, P, Q, R, S, T, U, V, W, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (n, o, p, q, r, s, t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function9<O, P, Q, R, S, T, U, V, W, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (o, p, q, r, s, t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function8<P, Q, R, S, T, U, V, W, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (p, q, r, s, t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function7<Q, R, S, T, U, V, W, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return (q, r, s, t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function6<R, S, T, U, V, W, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return (r, s, t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function5<S, T, U, V, W, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
	{
		return (s, t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function4<T, U, V, W, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
	{
		return (t, u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function3<U, V, W, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return (u, v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function2<V, W, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return (v, w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function1<W, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return (w) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RES> constR(W w)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RES> constR(V v, W w)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RES> constR(U u, V v, W w)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RES> constR(T t, U u, V v, W w)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RES> constR(S s, T t, U u, V v, W w)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RES> constR(R r, S s, T t, U u, V v, W w)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RES> constR(Q q, R r, S s, T t, U u, V v, W w)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RES> constR(P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RES> constR(O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RES> constR(N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function12<A, B, C, D, E, F, G, H, I, J, K, L, RES> constR(M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function11<A, B, C, D, E, F, G, H, I, J, K, RES> constR(L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function10<A, B, C, D, E, F, G, H, I, J, RES> constR(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function9<A, B, C, D, E, F, G, H, I, RES> constR(J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return (a, b, c, d, e, f, g, h, i) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function8<A, B, C, D, E, F, G, H, RES> constR(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return (a, b, c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function7<A, B, C, D, E, F, G, RES> constR(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return (a, b, c, d, e, f, g) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function6<A, B, C, D, E, F, RES> constR(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return (a, b, c, d, e, f) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function5<A, B, C, D, E, RES> constR(F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return (a, b, c, d, e) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function4<A, B, C, D, RES> constR(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return (a, b, c, d) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function3<A, B, C, RES> constR(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return (a, b, c) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function2<A, B, RES> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return (a, b) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	default Function1<A, RES> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return (a) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
}