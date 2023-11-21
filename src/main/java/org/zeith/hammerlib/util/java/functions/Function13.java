package org.zeith.hammerlib.util.java.functions;

import java.util.function.*;

import org.zeith.hammerlib.util.java.consumers.Consumer13;

@FunctionalInterface
public interface Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m);

	default <RES_MAPPED> Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> mapper.apply(apply(a, b, c, d, e, f, g, h, i, j, k, l, m));
	}

	default Consumer13<A, B, C, D, E, F, G, H, I, J, K, L, M> ignoreResult()
	{
		return this::apply;
	}

	default Function12<B, C, D, E, F, G, H, I, J, K, L, M, RES> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k, l, m) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function11<C, D, E, F, G, H, I, J, K, L, M, RES> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k, l, m) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function10<D, E, F, G, H, I, J, K, L, M, RES> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k, l, m) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function9<E, F, G, H, I, J, K, L, M, RES> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k, l, m) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function8<F, G, H, I, J, K, L, M, RES> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k, l, m) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function7<G, H, I, J, K, L, M, RES> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k, l, m) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function6<H, I, J, K, L, M, RES> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k, l, m) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function5<I, J, K, L, M, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k, l, m) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function4<J, K, L, M, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k, l, m) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function3<K, L, M, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k, l, m) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function2<L, M, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (l, m) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function1<M, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (m) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}

	default Function12<A, B, C, D, E, F, G, H, I, J, K, L, RES> constR(M m)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function11<A, B, C, D, E, F, G, H, I, J, K, RES> constR(L l, M m)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function10<A, B, C, D, E, F, G, H, I, J, RES> constR(K k, L l, M m)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function9<A, B, C, D, E, F, G, H, I, RES> constR(J j, K k, L l, M m)
	{
		return (a, b, c, d, e, f, g, h, i) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function8<A, B, C, D, E, F, G, H, RES> constR(I i, J j, K k, L l, M m)
	{
		return (a, b, c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function7<A, B, C, D, E, F, G, RES> constR(H h, I i, J j, K k, L l, M m)
	{
		return (a, b, c, d, e, f, g) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function6<A, B, C, D, E, F, RES> constR(G g, H h, I i, J j, K k, L l, M m)
	{
		return (a, b, c, d, e, f) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function5<A, B, C, D, E, RES> constR(F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (a, b, c, d, e) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function4<A, B, C, D, RES> constR(E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (a, b, c, d) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function3<A, B, C, RES> constR(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (a, b, c) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function2<A, B, RES> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (a, b) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Function1<A, RES> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (a) -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
}