package org.zeith.hammerlib.util.java.functions;

import org.zeith.hammerlib.util.java.consumers.Consumer12;

import java.util.function.Function;

@FunctionalInterface
public interface Function12<A, B, C, D, E, F, G, H, I, J, K, L, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l);

	default <RES_MAPPED> Function12<A, B, C, D, E, F, G, H, I, J, K, L, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> mapper.apply(apply(a, b, c, d, e, f, g, h, i, j, k, l));
	}
	
	default Consumer12<A, B, C, D, E, F, G, H, I, J, K, L> ignoreResult()
	{
		return this::apply;
	}
	
	default Function11<B, C, D, E, F, G, H, I, J, K, L, RES> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k, l) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function10<C, D, E, F, G, H, I, J, K, L, RES> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k, l) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function9<D, E, F, G, H, I, J, K, L, RES> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k, l) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function8<E, F, G, H, I, J, K, L, RES> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k, l) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function7<F, G, H, I, J, K, L, RES> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k, l) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function6<G, H, I, J, K, L, RES> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k, l) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function5<H, I, J, K, L, RES> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k, l) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function4<I, J, K, L, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k, l) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function3<J, K, L, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k, l) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function2<K, L, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k, l) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function1<L, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (l) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function11<A, B, C, D, E, F, G, H, I, J, K, RES> constR(L l)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function10<A, B, C, D, E, F, G, H, I, J, RES> constR(K k, L l)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function9<A, B, C, D, E, F, G, H, I, RES> constR(J j, K k, L l)
	{
		return (a, b, c, d, e, f, g, h, i) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function8<A, B, C, D, E, F, G, H, RES> constR(I i, J j, K k, L l)
	{
		return (a, b, c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function7<A, B, C, D, E, F, G, RES> constR(H h, I i, J j, K k, L l)
	{
		return (a, b, c, d, e, f, g) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function6<A, B, C, D, E, F, RES> constR(G g, H h, I i, J j, K k, L l)
	{
		return (a, b, c, d, e, f) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function5<A, B, C, D, E, RES> constR(F f, G g, H h, I i, J j, K k, L l)
	{
		return (a, b, c, d, e) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function4<A, B, C, D, RES> constR(E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (a, b, c, d) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function3<A, B, C, RES> constR(D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (a, b, c) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function2<A, B, RES> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (a, b) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Function1<A, RES> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (a) -> apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
}