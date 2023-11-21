package org.zeith.hammerlib.util.java.functions;

import org.zeith.hammerlib.util.java.consumers.Consumer7;

import java.util.function.Function;

@FunctionalInterface
public interface Function7<A, B, C, D, E, F, G, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f, G g);

	default <RES_MAPPED> Function7<A, B, C, D, E, F, G, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f, g) -> mapper.apply(apply(a, b, c, d, e, f, g));
	}
	
	default Consumer7<A, B, C, D, E, F, G> ignoreResult()
	{
		return this::apply;
	}
	
	default Function6<B, C, D, E, F, G, RES> constL(A a)
	{
		return (b, c, d, e, f, g) -> apply(a, b, c, d, e, f, g);
	}
	
	default Function5<C, D, E, F, G, RES> constL(A a, B b)
	{
		return (c, d, e, f, g) -> apply(a, b, c, d, e, f, g);
	}
	
	default Function4<D, E, F, G, RES> constL(A a, B b, C c)
	{
		return (d, e, f, g) -> apply(a, b, c, d, e, f, g);
	}
	
	default Function3<E, F, G, RES> constL(A a, B b, C c, D d)
	{
		return (e, f, g) -> apply(a, b, c, d, e, f, g);
	}
	
	default Function2<F, G, RES> constL(A a, B b, C c, D d, E e)
	{
		return (f, g) -> apply(a, b, c, d, e, f, g);
	}
	
	default Function1<G, RES> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g) -> apply(a, b, c, d, e, f, g);
	}
	
	default Function6<A, B, C, D, E, F, RES> constR(G g)
	{
		return (a, b, c, d, e, f) -> apply(a, b, c, d, e, f, g);
	}
	
	default Function5<A, B, C, D, E, RES> constR(F f, G g)
	{
		return (a, b, c, d, e) -> apply(a, b, c, d, e, f, g);
	}
	
	default Function4<A, B, C, D, RES> constR(E e, F f, G g)
	{
		return (a, b, c, d) -> apply(a, b, c, d, e, f, g);
	}
	
	default Function3<A, B, C, RES> constR(D d, E e, F f, G g)
	{
		return (a, b, c) -> apply(a, b, c, d, e, f, g);
	}
	
	default Function2<A, B, RES> constR(C c, D d, E e, F f, G g)
	{
		return (a, b) -> apply(a, b, c, d, e, f, g);
	}
	
	default Function1<A, RES> constR(B b, C c, D d, E e, F f, G g)
	{
		return (a) -> apply(a, b, c, d, e, f, g);
	}
}