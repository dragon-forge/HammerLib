package org.zeith.hammerlib.util.java.functions;

import org.zeith.hammerlib.util.java.consumers.Consumer15;

import java.util.function.Function;

@FunctionalInterface
public interface Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o);

	default <RES_MAPPED> Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> mapper.apply(apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o));
	}
	
	default Consumer15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> ignoreResult()
	{
		return this::apply;
	}
	
	default Function14<B, C, D, E, F, G, H, I, J, K, L, M, N, O, RES> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function13<C, D, E, F, G, H, I, J, K, L, M, N, O, RES> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k, l, m, n, o) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function12<D, E, F, G, H, I, J, K, L, M, N, O, RES> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k, l, m, n, o) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function11<E, F, G, H, I, J, K, L, M, N, O, RES> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k, l, m, n, o) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function10<F, G, H, I, J, K, L, M, N, O, RES> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k, l, m, n, o) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function9<G, H, I, J, K, L, M, N, O, RES> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k, l, m, n, o) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function8<H, I, J, K, L, M, N, O, RES> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k, l, m, n, o) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function7<I, J, K, L, M, N, O, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k, l, m, n, o) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function6<J, K, L, M, N, O, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k, l, m, n, o) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function5<K, L, M, N, O, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k, l, m, n, o) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function4<L, M, N, O, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (l, m, n, o) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function3<M, N, O, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (m, n, o) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function2<N, O, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (n, o) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function1<O, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (o) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RES> constR(O o)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RES> constR(N n, O o)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function12<A, B, C, D, E, F, G, H, I, J, K, L, RES> constR(M m, N n, O o)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function11<A, B, C, D, E, F, G, H, I, J, K, RES> constR(L l, M m, N n, O o)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function10<A, B, C, D, E, F, G, H, I, J, RES> constR(K k, L l, M m, N n, O o)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function9<A, B, C, D, E, F, G, H, I, RES> constR(J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c, d, e, f, g, h, i) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function8<A, B, C, D, E, F, G, H, RES> constR(I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function7<A, B, C, D, E, F, G, RES> constR(H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c, d, e, f, g) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function6<A, B, C, D, E, F, RES> constR(G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c, d, e, f) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function5<A, B, C, D, E, RES> constR(F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c, d, e) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function4<A, B, C, D, RES> constR(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c, d) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function3<A, B, C, RES> constR(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function2<A, B, RES> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Function1<A, RES> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
}