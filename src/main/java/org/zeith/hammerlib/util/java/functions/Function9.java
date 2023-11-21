package org.zeith.hammerlib.util.java.functions;

import org.zeith.hammerlib.util.java.consumers.Consumer9;

import java.util.function.Function;

@FunctionalInterface
public interface Function9<A, B, C, D, E, F, G, H, I, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f, G g, H h, I i);

	default <RES_MAPPED> Function9<A, B, C, D, E, F, G, H, I, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f, g, h, i) -> mapper.apply(apply(a, b, c, d, e, f, g, h, i));
	}
	
	default Consumer9<A, B, C, D, E, F, G, H, I> ignoreResult()
	{
		return this::apply;
	}
	
	default Function8<B, C, D, E, F, G, H, I, RES> constL(A a)
	{
		return (b, c, d, e, f, g, h, i) -> apply(a, b, c, d, e, f, g, h, i);
	}
	
	default Function7<C, D, E, F, G, H, I, RES> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i) -> apply(a, b, c, d, e, f, g, h, i);
	}
	
	default Function6<D, E, F, G, H, I, RES> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i) -> apply(a, b, c, d, e, f, g, h, i);
	}
	
	default Function5<E, F, G, H, I, RES> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i) -> apply(a, b, c, d, e, f, g, h, i);
	}
	
	default Function4<F, G, H, I, RES> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i) -> apply(a, b, c, d, e, f, g, h, i);
	}
	
	default Function3<G, H, I, RES> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i) -> apply(a, b, c, d, e, f, g, h, i);
	}
	
	default Function2<H, I, RES> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i) -> apply(a, b, c, d, e, f, g, h, i);
	}
	
	default Function1<I, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i) -> apply(a, b, c, d, e, f, g, h, i);
	}
	
	default Function8<A, B, C, D, E, F, G, H, RES> constR(I i)
	{
		return (a, b, c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h, i);
	}
	
	default Function7<A, B, C, D, E, F, G, RES> constR(H h, I i)
	{
		return (a, b, c, d, e, f, g) -> apply(a, b, c, d, e, f, g, h, i);
	}
	
	default Function6<A, B, C, D, E, F, RES> constR(G g, H h, I i)
	{
		return (a, b, c, d, e, f) -> apply(a, b, c, d, e, f, g, h, i);
	}
	
	default Function5<A, B, C, D, E, RES> constR(F f, G g, H h, I i)
	{
		return (a, b, c, d, e) -> apply(a, b, c, d, e, f, g, h, i);
	}
	
	default Function4<A, B, C, D, RES> constR(E e, F f, G g, H h, I i)
	{
		return (a, b, c, d) -> apply(a, b, c, d, e, f, g, h, i);
	}
	
	default Function3<A, B, C, RES> constR(D d, E e, F f, G g, H h, I i)
	{
		return (a, b, c) -> apply(a, b, c, d, e, f, g, h, i);
	}
	
	default Function2<A, B, RES> constR(C c, D d, E e, F f, G g, H h, I i)
	{
		return (a, b) -> apply(a, b, c, d, e, f, g, h, i);
	}
	
	default Function1<A, RES> constR(B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (a) -> apply(a, b, c, d, e, f, g, h, i);
	}
}