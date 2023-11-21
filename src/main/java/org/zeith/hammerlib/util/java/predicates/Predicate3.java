package org.zeith.hammerlib.util.java.predicates;

import java.util.Objects;

@FunctionalInterface
public interface Predicate3<A, B, C>
{
	boolean test(A a, B b, C c);
	
	default Predicate3<A, B, C> and(Predicate3<? super A, ? super B, ? super C> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c) -> test(a, b, c) && other.test(a, b, c);
	}
	
	default Predicate3<A, B, C> negate()
	{
		return (a, b, c) -> !test(a, b, c);
	}
	
	default Predicate3<A, B, C> or(Predicate3<? super A, ? super B, ? super C> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c) -> test(a, b, c) || other.test(a, b, c);
	}
	
	default Predicate2<B, C> constL(A a)
	{
		return (b, c) -> test(a, b, c);
	}
	
	default Predicate1<C> constL(A a, B b)
	{
		return (c) -> test(a, b, c);
	}
	
	default Predicate2<A, B> constR(C c)
	{
		return (a, b) -> test(a, b, c);
	}
	
	default Predicate1<A> constR(B b, C c)
	{
		return (a) -> test(a, b, c);
	}
}