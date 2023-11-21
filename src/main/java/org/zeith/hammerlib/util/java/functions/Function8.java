package org.zeith.hammerlib.util.java.functions;

import java.util.function.*;

import org.zeith.hammerlib.util.java.consumers.Consumer8;

@FunctionalInterface
public interface Function8<A, B, C, D, E, F, G, H, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f, G g, H h);

	default <RES_MAPPED> Function8<A, B, C, D, E, F, G, H, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f, g, h) -> mapper.apply(apply(a, b, c, d, e, f, g, h));
	}

	default Consumer8<A, B, C, D, E, F, G, H> ignoreResult()
	{
		return this::apply;
	}

	default Function7<B, C, D, E, F, G, H, RES> constL(A a)
	{
		return (b, c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
	}
	
	default Function6<C, D, E, F, G, H, RES> constL(A a, B b)
	{
		return (c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
	}
	
	default Function5<D, E, F, G, H, RES> constL(A a, B b, C c)
	{
		return (d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
	}
	
	default Function4<E, F, G, H, RES> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
	}
	
	default Function3<F, G, H, RES> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h) -> apply(a, b, c, d, e, f, g, h);
	}
	
	default Function2<G, H, RES> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h) -> apply(a, b, c, d, e, f, g, h);
	}
	
	default Function1<H, RES> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h) -> apply(a, b, c, d, e, f, g, h);
	}

	default Function7<A, B, C, D, E, F, G, RES> constR(H h)
	{
		return (a, b, c, d, e, f, g) -> apply(a, b, c, d, e, f, g, h);
	}
	
	default Function6<A, B, C, D, E, F, RES> constR(G g, H h)
	{
		return (a, b, c, d, e, f) -> apply(a, b, c, d, e, f, g, h);
	}
	
	default Function5<A, B, C, D, E, RES> constR(F f, G g, H h)
	{
		return (a, b, c, d, e) -> apply(a, b, c, d, e, f, g, h);
	}
	
	default Function4<A, B, C, D, RES> constR(E e, F f, G g, H h)
	{
		return (a, b, c, d) -> apply(a, b, c, d, e, f, g, h);
	}
	
	default Function3<A, B, C, RES> constR(D d, E e, F f, G g, H h)
	{
		return (a, b, c) -> apply(a, b, c, d, e, f, g, h);
	}
	
	default Function2<A, B, RES> constR(C c, D d, E e, F f, G g, H h)
	{
		return (a, b) -> apply(a, b, c, d, e, f, g, h);
	}
	
	default Function1<A, RES> constR(B b, C c, D d, E e, F f, G g, H h)
	{
		return (a) -> apply(a, b, c, d, e, f, g, h);
	}
}