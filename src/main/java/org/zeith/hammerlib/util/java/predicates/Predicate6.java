package org.zeith.hammerlib.util.java.predicates;

import java.util.Objects;

@FunctionalInterface
public interface Predicate6<A, B, C, D, E, F>
{
	boolean test(A a, B b, C c, D d, E e, F f);
	
	default Predicate6<A, B, C, D, E, F> and(Predicate6<? super A, ? super B, ? super C, ? super D, ? super E, ? super F> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f) -> test(a, b, c, d, e, f) && other.test(a, b, c, d, e, f);
	}
	
	default Predicate6<A, B, C, D, E, F> negate()
	{
		return (a, b, c, d, e, f) -> !test(a, b, c, d, e, f);
	}
	
	default Predicate6<A, B, C, D, E, F> or(Predicate6<? super A, ? super B, ? super C, ? super D, ? super E, ? super F> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f) -> test(a, b, c, d, e, f) || other.test(a, b, c, d, e, f);
	}
	
	default Predicate5<B, C, D, E, F> constL(A a)
	{
		return (b, c, d, e, f) -> test(a, b, c, d, e, f);
	}
	
	default Predicate4<C, D, E, F> constL(A a, B b)
	{
		return (c, d, e, f) -> test(a, b, c, d, e, f);
	}
	
	default Predicate3<D, E, F> constL(A a, B b, C c)
	{
		return (d, e, f) -> test(a, b, c, d, e, f);
	}
	
	default Predicate2<E, F> constL(A a, B b, C c, D d)
	{
		return (e, f) -> test(a, b, c, d, e, f);
	}
	
	default Predicate1<F> constL(A a, B b, C c, D d, E e)
	{
		return (f) -> test(a, b, c, d, e, f);
	}
	
	default Predicate5<A, B, C, D, E> constR(F f)
	{
		return (a, b, c, d, e) -> test(a, b, c, d, e, f);
	}
	
	default Predicate4<A, B, C, D> constR(E e, F f)
	{
		return (a, b, c, d) -> test(a, b, c, d, e, f);
	}
	
	default Predicate3<A, B, C> constR(D d, E e, F f)
	{
		return (a, b, c) -> test(a, b, c, d, e, f);
	}
	
	default Predicate2<A, B> constR(C c, D d, E e, F f)
	{
		return (a, b) -> test(a, b, c, d, e, f);
	}
	
	default Predicate1<A> constR(B b, C c, D d, E e, F f)
	{
		return (a) -> test(a, b, c, d, e, f);
	}
}