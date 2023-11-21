package org.zeith.hammerlib.util.java.predicates;

import java.util.Objects;

@FunctionalInterface
public interface Predicate8<A, B, C, D, E, F, G, H>
{
	boolean test(A a, B b, C c, D d, E e, F f, G g, H h);

	default Predicate8<A, B, C, D, E, F, G, H> and(Predicate8<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h) -> test(a, b, c, d, e, f, g, h) && other.test(a, b, c, d, e, f, g, h);
	}

	default Predicate8<A, B, C, D, E, F, G, H> negate()
	{
		return (a, b, c, d, e, f, g, h) -> !test(a, b, c, d, e, f, g, h);
	}

	default Predicate8<A, B, C, D, E, F, G, H> or(Predicate8<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h) -> test(a, b, c, d, e, f, g, h) || other.test(a, b, c, d, e, f, g, h);
	}

	default Predicate7<B, C, D, E, F, G, H> constL(A a)
	{
		return (b, c, d, e, f, g, h) -> test(a, b, c, d, e, f, g, h);
	}
	
	default Predicate6<C, D, E, F, G, H> constL(A a, B b)
	{
		return (c, d, e, f, g, h) -> test(a, b, c, d, e, f, g, h);
	}
	
	default Predicate5<D, E, F, G, H> constL(A a, B b, C c)
	{
		return (d, e, f, g, h) -> test(a, b, c, d, e, f, g, h);
	}
	
	default Predicate4<E, F, G, H> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h) -> test(a, b, c, d, e, f, g, h);
	}
	
	default Predicate3<F, G, H> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h) -> test(a, b, c, d, e, f, g, h);
	}
	
	default Predicate2<G, H> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h) -> test(a, b, c, d, e, f, g, h);
	}
	
	default Predicate1<H> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h) -> test(a, b, c, d, e, f, g, h);
	}

	default Predicate7<A, B, C, D, E, F, G> constR(H h)
	{
		return (a, b, c, d, e, f, g) -> test(a, b, c, d, e, f, g, h);
	}
	
	default Predicate6<A, B, C, D, E, F> constR(G g, H h)
	{
		return (a, b, c, d, e, f) -> test(a, b, c, d, e, f, g, h);
	}
	
	default Predicate5<A, B, C, D, E> constR(F f, G g, H h)
	{
		return (a, b, c, d, e) -> test(a, b, c, d, e, f, g, h);
	}
	
	default Predicate4<A, B, C, D> constR(E e, F f, G g, H h)
	{
		return (a, b, c, d) -> test(a, b, c, d, e, f, g, h);
	}
	
	default Predicate3<A, B, C> constR(D d, E e, F f, G g, H h)
	{
		return (a, b, c) -> test(a, b, c, d, e, f, g, h);
	}
	
	default Predicate2<A, B> constR(C c, D d, E e, F f, G g, H h)
	{
		return (a, b) -> test(a, b, c, d, e, f, g, h);
	}
	
	default Predicate1<A> constR(B b, C c, D d, E e, F f, G g, H h)
	{
		return (a) -> test(a, b, c, d, e, f, g, h);
	}
}