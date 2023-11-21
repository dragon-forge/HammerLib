package org.zeith.hammerlib.util.java.functions;

import org.zeith.hammerlib.util.java.consumers.Consumer11;

import java.util.function.Function;

@FunctionalInterface
public interface Function11<A, B, C, D, E, F, G, H, I, J, K, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k);

	default <RES_MAPPED> Function11<A, B, C, D, E, F, G, H, I, J, K, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> mapper.apply(apply(a, b, c, d, e, f, g, h, i, j, k));
	}
	
	default Consumer11<A, B, C, D, E, F, G, H, I, J, K> ignoreResult()
	{
		return this::apply;
	}
	
	default Function10<B, C, D, E, F, G, H, I, J, K, RES> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function9<C, D, E, F, G, H, I, J, K, RES> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function8<D, E, F, G, H, I, J, K, RES> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function7<E, F, G, H, I, J, K, RES> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function6<F, G, H, I, J, K, RES> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function5<G, H, I, J, K, RES> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function4<H, I, J, K, RES> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function3<I, J, K, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function2<J, K, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function1<K, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function10<A, B, C, D, E, F, G, H, I, J, RES> constR(K k)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function9<A, B, C, D, E, F, G, H, I, RES> constR(J j, K k)
	{
		return (a, b, c, d, e, f, g, h, i) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function8<A, B, C, D, E, F, G, H, RES> constR(I i, J j, K k)
	{
		return (a, b, c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function7<A, B, C, D, E, F, G, RES> constR(H h, I i, J j, K k)
	{
		return (a, b, c, d, e, f, g) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function6<A, B, C, D, E, F, RES> constR(G g, H h, I i, J j, K k)
	{
		return (a, b, c, d, e, f) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function5<A, B, C, D, E, RES> constR(F f, G g, H h, I i, J j, K k)
	{
		return (a, b, c, d, e) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function4<A, B, C, D, RES> constR(E e, F f, G g, H h, I i, J j, K k)
	{
		return (a, b, c, d) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function3<A, B, C, RES> constR(D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (a, b, c) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function2<A, B, RES> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (a, b) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Function1<A, RES> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (a) -> apply(a, b, c, d, e, f, g, h, i, j, k);
	}
}