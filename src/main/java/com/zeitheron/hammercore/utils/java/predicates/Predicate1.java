package com.zeitheron.hammercore.utils.java.predicates;


import java.util.function.Predicate;
import java.util.Objects;

@FunctionalInterface
public interface Predicate1<A>
		extends Predicate<A>
{
	@Override
	boolean test(A a);

	default Predicate1<A> and(Predicate1<? super A> other)
	{
		Objects.requireNonNull(other);
		return (a) -> test(a) && other.test(a);
	}

	default Predicate1<A> negate()
	{
		return (a) -> !test(a);
	}

	default Predicate1<A> or(Predicate1<? super A> other)
	{
		Objects.requireNonNull(other);
		return (a) -> test(a) || other.test(a);
	}
}