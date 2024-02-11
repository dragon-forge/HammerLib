package com.zeitheron.hammercore.utils.java.predicates;


import java.util.function.BiPredicate;
import java.util.Objects;

@FunctionalInterface
public interface Predicate2<A, B>
		extends BiPredicate<A, B>
{
	@Override
	boolean test(A a, B b);

	default Predicate2<A, B> and(Predicate2<? super A, ? super B> other)
	{
		Objects.requireNonNull(other);
		return (a, b) -> test(a, b) && other.test(a, b);
	}

	default Predicate2<A, B> negate()
	{
		return (a, b) -> !test(a, b);
	}

	default Predicate2<A, B> or(Predicate2<? super A, ? super B> other)
	{
		Objects.requireNonNull(other);
		return (a, b) -> test(a, b) || other.test(a, b);
	}

	default Predicate1<B> constL(A a)
	{
		return (b) -> test(a, b);
	}

	default Predicate1<A> constR(B b)
	{
		return (a) -> test(a, b);
	}
}