package org.zeith.hammerlib.util.java.functions;

import java.util.function.*;

import org.zeith.hammerlib.util.java.consumers.Consumer17;

@FunctionalInterface
public interface Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q);

	default <RES_MAPPED> Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q) -> mapper.apply(apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q));
	}

	default Consumer17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> ignoreResult()
	{
		return this::apply;
	}

	default Function16<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RES> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function15<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RES> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k, l, m, n, o, p, q) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function14<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RES> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k, l, m, n, o, p, q) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function13<E, F, G, H, I, J, K, L, M, N, O, P, Q, RES> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k, l, m, n, o, p, q) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function12<F, G, H, I, J, K, L, M, N, O, P, Q, RES> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k, l, m, n, o, p, q) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function11<G, H, I, J, K, L, M, N, O, P, Q, RES> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k, l, m, n, o, p, q) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function10<H, I, J, K, L, M, N, O, P, Q, RES> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k, l, m, n, o, p, q) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function9<I, J, K, L, M, N, O, P, Q, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k, l, m, n, o, p, q) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function8<J, K, L, M, N, O, P, Q, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k, l, m, n, o, p, q) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function7<K, L, M, N, O, P, Q, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k, l, m, n, o, p, q) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function6<L, M, N, O, P, Q, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (l, m, n, o, p, q) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function5<M, N, O, P, Q, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (m, n, o, p, q) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function4<N, O, P, Q, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (n, o, p, q) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function3<O, P, Q, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (o, p, q) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function2<P, Q, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (p, q) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function1<Q, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return (q) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}

	default Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RES> constR(Q q)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RES> constR(P p, Q q)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RES> constR(O o, P p, Q q)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RES> constR(N n, O o, P p, Q q)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function12<A, B, C, D, E, F, G, H, I, J, K, L, RES> constR(M m, N n, O o, P p, Q q)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function11<A, B, C, D, E, F, G, H, I, J, K, RES> constR(L l, M m, N n, O o, P p, Q q)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function10<A, B, C, D, E, F, G, H, I, J, RES> constR(K k, L l, M m, N n, O o, P p, Q q)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function9<A, B, C, D, E, F, G, H, I, RES> constR(J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return (a, b, c, d, e, f, g, h, i) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function8<A, B, C, D, E, F, G, H, RES> constR(I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return (a, b, c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function7<A, B, C, D, E, F, G, RES> constR(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return (a, b, c, d, e, f, g) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function6<A, B, C, D, E, F, RES> constR(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return (a, b, c, d, e, f) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function5<A, B, C, D, E, RES> constR(F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return (a, b, c, d, e) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function4<A, B, C, D, RES> constR(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return (a, b, c, d) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function3<A, B, C, RES> constR(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return (a, b, c) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function2<A, B, RES> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return (a, b) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	default Function1<A, RES> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return (a) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
}