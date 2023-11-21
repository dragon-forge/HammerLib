package org.zeith.hammerlib.util.java.predicates;

import java.util.Objects;

@FunctionalInterface
public interface Predicate5<A, B, C, D, E>
{
	boolean test(A a, B b, C c, D d, E e);

	default Predicate5<A, B, C, D, E> and(Predicate5<? super A, ? super B, ? super C, ? super D, ? super E> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e) -> test(a, b, c, d, e) && other.test(a, b, c, d, e);
	}

	default Predicate5<A, B, C, D, E> negate()
	{
		return (a, b, c, d, e) -> !test(a, b, c, d, e);
	}

	default Predicate5<A, B, C, D, E> or(Predicate5<? super A, ? super B, ? super C, ? super D, ? super E> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e) -> test(a, b, c, d, e) || other.test(a, b, c, d, e);
	}

	default Predicate4<B, C, D, E> constL(A a)
	{
		return (b, c, d, e) -> test(a, b, c, d, e);
	}
	
	default Predicate3<C, D, E> constL(A a, B b)
	{
		return (c, d, e) -> test(a, b, c, d, e);
	}
	
	default Predicate2<D, E> constL(A a, B b, C c)
	{
		return (d, e) -> test(a, b, c, d, e);
	}
	
	default Predicate1<E> constL(A a, B b, C c, D d)
	{
		return (e) -> test(a, b, c, d, e);
	}

	default Predicate4<A, B, C, D> constR(E e)
	{
		return (a, b, c, d) -> test(a, b, c, d, e);
	}
	
	default Predicate3<A, B, C> constR(D d, E e)
	{
		return (a, b, c) -> test(a, b, c, d, e);
	}
	
	default Predicate2<A, B> constR(C c, D d, E e)
	{
		return (a, b) -> test(a, b, c, d, e);
	}
	
	default Predicate1<A> constR(B b, C c, D d, E e)
	{
		return (a) -> test(a, b, c, d, e);
	}
}