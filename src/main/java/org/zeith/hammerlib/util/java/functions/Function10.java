package org.zeith.hammerlib.util.java.functions;

import org.zeith.hammerlib.util.java.consumers.Consumer10;

import java.util.function.Function;

@FunctionalInterface
public interface Function10<A, B, C, D, E, F, G, H, I, J, RES>
{
	RES apply(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j);

	default <RES_MAPPED> Function10<A, B, C, D, E, F, G, H, I, J, RES_MAPPED> map(Function<RES, RES_MAPPED> mapper)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> mapper.apply(apply(a, b, c, d, e, f, g, h, i, j));
	}
	
	default Consumer10<A, B, C, D, E, F, G, H, I, J> ignoreResult()
	{
		return this::apply;
	}
	
	default Function9<B, C, D, E, F, G, H, I, J, RES> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j) -> apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Function8<C, D, E, F, G, H, I, J, RES> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j) -> apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Function7<D, E, F, G, H, I, J, RES> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j) -> apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Function6<E, F, G, H, I, J, RES> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j) -> apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Function5<F, G, H, I, J, RES> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j) -> apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Function4<G, H, I, J, RES> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j) -> apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Function3<H, I, J, RES> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j) -> apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Function2<I, J, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j) -> apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Function1<J, RES> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j) -> apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Function9<A, B, C, D, E, F, G, H, I, RES> constR(J j)
	{
		return (a, b, c, d, e, f, g, h, i) -> apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Function8<A, B, C, D, E, F, G, H, RES> constR(I i, J j)
	{
		return (a, b, c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Function7<A, B, C, D, E, F, G, RES> constR(H h, I i, J j)
	{
		return (a, b, c, d, e, f, g) -> apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Function6<A, B, C, D, E, F, RES> constR(G g, H h, I i, J j)
	{
		return (a, b, c, d, e, f) -> apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Function5<A, B, C, D, E, RES> constR(F f, G g, H h, I i, J j)
	{
		return (a, b, c, d, e) -> apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Function4<A, B, C, D, RES> constR(E e, F f, G g, H h, I i, J j)
	{
		return (a, b, c, d) -> apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Function3<A, B, C, RES> constR(D d, E e, F f, G g, H h, I i, J j)
	{
		return (a, b, c) -> apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Function2<A, B, RES> constR(C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (a, b) -> apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Function1<A, RES> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (a) -> apply(a, b, c, d, e, f, g, h, i, j);
	}
}