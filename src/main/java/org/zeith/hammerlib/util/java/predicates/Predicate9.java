package org.zeith.hammerlib.util.java.predicates;

import java.util.Objects;

@FunctionalInterface
public interface Predicate9<A, B, C, D, E, F, G, H, I>
{
	boolean test(A a, B b, C c, D d, E e, F f, G g, H h, I i);

	default Predicate9<A, B, C, D, E, F, G, H, I> and(Predicate9<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i) -> test(a, b, c, d, e, f, g, h, i) && other.test(a, b, c, d, e, f, g, h, i);
	}

	default Predicate9<A, B, C, D, E, F, G, H, I> negate()
	{
		return (a, b, c, d, e, f, g, h, i) -> !test(a, b, c, d, e, f, g, h, i);
	}

	default Predicate9<A, B, C, D, E, F, G, H, I> or(Predicate9<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i) -> test(a, b, c, d, e, f, g, h, i) || other.test(a, b, c, d, e, f, g, h, i);
	}

	default Predicate8<B, C, D, E, F, G, H, I> constL(A a)
	{
		return (b, c, d, e, f, g, h, i) -> test(a, b, c, d, e, f, g, h, i);
	}
	
	default Predicate7<C, D, E, F, G, H, I> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i) -> test(a, b, c, d, e, f, g, h, i);
	}
	
	default Predicate6<D, E, F, G, H, I> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i) -> test(a, b, c, d, e, f, g, h, i);
	}
	
	default Predicate5<E, F, G, H, I> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i) -> test(a, b, c, d, e, f, g, h, i);
	}
	
	default Predicate4<F, G, H, I> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i) -> test(a, b, c, d, e, f, g, h, i);
	}
	
	default Predicate3<G, H, I> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i) -> test(a, b, c, d, e, f, g, h, i);
	}
	
	default Predicate2<H, I> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i) -> test(a, b, c, d, e, f, g, h, i);
	}
	
	default Predicate1<I> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i) -> test(a, b, c, d, e, f, g, h, i);
	}

	default Predicate8<A, B, C, D, E, F, G, H> constR(I i)
	{
		return (a, b, c, d, e, f, g, h) -> test(a, b, c, d, e, f, g, h, i);
	}
	
	default Predicate7<A, B, C, D, E, F, G> constR(H h, I i)
	{
		return (a, b, c, d, e, f, g) -> test(a, b, c, d, e, f, g, h, i);
	}
	
	default Predicate6<A, B, C, D, E, F> constR(G g, H h, I i)
	{
		return (a, b, c, d, e, f) -> test(a, b, c, d, e, f, g, h, i);
	}
	
	default Predicate5<A, B, C, D, E> constR(F f, G g, H h, I i)
	{
		return (a, b, c, d, e) -> test(a, b, c, d, e, f, g, h, i);
	}
	
	default Predicate4<A, B, C, D> constR(E e, F f, G g, H h, I i)
	{
		return (a, b, c, d) -> test(a, b, c, d, e, f, g, h, i);
	}
	
	default Predicate3<A, B, C> constR(D d, E e, F f, G g, H h, I i)
	{
		return (a, b, c) -> test(a, b, c, d, e, f, g, h, i);
	}
	
	default Predicate2<A, B> constR(C c, D d, E e, F f, G g, H h, I i)
	{
		return (a, b) -> test(a, b, c, d, e, f, g, h, i);
	}
	
	default Predicate1<A> constR(B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (a) -> test(a, b, c, d, e, f, g, h, i);
	}
}