package org.zeith.hammerlib.util.java;

import net.minecraft.util.Tuple;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class StreamHelper
{
	public static <S, V> Function<S, Tuple<S, V>> inject(V value)
	{
		return s -> new Tuple<>(s, value);
	}

	public static <S, V, T> Function<Tuple<S, V>, T> transform(BiFunction<S, V, T> transformer)
	{
		return t -> transformer.apply(t.getA(), t.getB());
	}

	public static <S, V> Predicate<Tuple<S, V>> filter(BiPredicate<S, V> transformer)
	{
		return t -> transformer.test(t.getA(), t.getB());
	}
}