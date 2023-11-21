package org.zeith.hammerlib.util.java.predicates;

import java.util.Objects;

@FunctionalInterface
public interface Predicate7<A, B, C, D, E, F, G>
{
	boolean test(A a, B b, C c, D d, E e, F f, G g);

	default Predicate7<A, B, C, D, E, F, G> and(Predicate7<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g) -> test(a, b, c, d, e, f, g) && other.test(a, b, c, d, e, f, g);
	}

	default Predicate7<A, B, C, D, E, F, G> negate()
	{
		return (a, b, c, d, e, f, g) -> !test(a, b, c, d, e, f, g);
	}

	default Predicate7<A, B, C, D, E, F, G> or(Predicate7<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g) -> test(a, b, c, d, e, f, g) || other.test(a, b, c, d, e, f, g);
	}

	default Predicate6<B, C, D, E, F, G> constL(A a)
	{
		return (b, c, d, e, f, g) -> test(a, b, c, d, e, f, g);
	}
	
	default Predicate5<C, D, E, F, G> constL(A a, B b)
	{
		return (c, d, e, f, g) -> test(a, b, c, d, e, f, g);
	}
	
	default Predicate4<D, E, F, G> constL(A a, B b, C c)
	{
		return (d, e, f, g) -> test(a, b, c, d, e, f, g);
	}
	
	default Predicate3<E, F, G> constL(A a, B b, C c, D d)
	{
		return (e, f, g) -> test(a, b, c, d, e, f, g);
	}
	
	default Predicate2<F, G> constL(A a, B b, C c, D d, E e)
	{
		return (f, g) -> test(a, b, c, d, e, f, g);
	}
	
	default Predicate1<G> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g) -> test(a, b, c, d, e, f, g);
	}

	default Predicate6<A, B, C, D, E, F> constR(G g)
	{
		return (a, b, c, d, e, f) -> test(a, b, c, d, e, f, g);
	}
	
	default Predicate5<A, B, C, D, E> constR(F f, G g)
	{
		return (a, b, c, d, e) -> test(a, b, c, d, e, f, g);
	}
	
	default Predicate4<A, B, C, D> constR(E e, F f, G g)
	{
		return (a, b, c, d) -> test(a, b, c, d, e, f, g);
	}
	
	default Predicate3<A, B, C> constR(D d, E e, F f, G g)
	{
		return (a, b, c) -> test(a, b, c, d, e, f, g);
	}
	
	default Predicate2<A, B> constR(C c, D d, E e, F f, G g)
	{
		return (a, b) -> test(a, b, c, d, e, f, g);
	}
	
	default Predicate1<A> constR(B b, C c, D d, E e, F f, G g)
	{
		return (a) -> test(a, b, c, d, e, f, g);
	}
}