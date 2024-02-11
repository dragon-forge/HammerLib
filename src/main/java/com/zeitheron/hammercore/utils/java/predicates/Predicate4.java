package com.zeitheron.hammercore.utils.java.predicates;

import java.util.Objects;

@FunctionalInterface
public interface Predicate4<A, B, C, D>
{
	boolean test(A a, B b, C c, D d);

	default Predicate4<A, B, C, D> and(Predicate4<? super A, ? super B, ? super C, ? super D> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d) -> test(a, b, c, d) && other.test(a, b, c, d);
	}

	default Predicate4<A, B, C, D> negate()
	{
		return (a, b, c, d) -> !test(a, b, c, d);
	}

	default Predicate4<A, B, C, D> or(Predicate4<? super A, ? super B, ? super C, ? super D> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d) -> test(a, b, c, d) || other.test(a, b, c, d);
	}

	default Predicate3<B, C, D> constL(A a)
	{
		return (b, c, d) -> test(a, b, c, d);
	}
	
	default Predicate2<C, D> constL(A a, B b)
	{
		return (c, d) -> test(a, b, c, d);
	}
	
	default Predicate1<D> constL(A a, B b, C c)
	{
		return (d) -> test(a, b, c, d);
	}

	default Predicate3<A, B, C> constR(D d)
	{
		return (a, b, c) -> test(a, b, c, d);
	}
	
	default Predicate2<A, B> constR(C c, D d)
	{
		return (a, b) -> test(a, b, c, d);
	}
	
	default Predicate1<A> constR(B b, C c, D d)
	{
		return (a) -> test(a, b, c, d);
	}
}