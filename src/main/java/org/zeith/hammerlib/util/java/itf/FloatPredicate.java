package org.zeith.hammerlib.util.java.itf;

import java.util.Objects;

@FunctionalInterface
public interface FloatPredicate
{
	/**
	 * Evaluates this predicate on the given argument.
	 *
	 * @param value
	 * 		the input argument
	 *
	 * @return {@code true} if the input argument matches the predicate,
	 * otherwise {@code false}
	 */
	boolean test(float value);
	
	default FloatPredicate and(FloatPredicate other)
	{
		Objects.requireNonNull(other);
		return (value) -> test(value) && other.test(value);
	}
	
	default FloatPredicate negate()
	{
		return (value) -> !test(value);
	}
	
	default FloatPredicate or(FloatPredicate other)
	{
		Objects.requireNonNull(other);
		return (value) -> test(value) || other.test(value);
	}
}
