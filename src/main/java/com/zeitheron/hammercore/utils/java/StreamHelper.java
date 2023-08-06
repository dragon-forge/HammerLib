package com.zeitheron.hammercore.utils.java;

import net.minecraft.util.Tuple;

import java.util.Optional;
import java.util.function.*;
import java.util.stream.Stream;

public class StreamHelper
{
	public static <V> Stream<V> optionalStream(Optional<V> opt)
	{
		return opt.map(Stream::of).orElseGet(Stream::empty);
	}
	
	public static <S, V> Function<S, Tuple<S, V>> inject(V value)
	{
		return s -> new Tuple<>(s, value);
	}
	
	public static <S, V, T> Function<Tuple<S, V>, T> transform(BiFunction<S, V, T> transformer)
	{
		return t -> transformer.apply(t.getFirst(), t.getSecond());
	}
	
	public static <S, V> Predicate<Tuple<S, V>> filter(BiPredicate<S, V> transformer)
	{
		return t -> transformer.test(t.getFirst(), t.getSecond());
	}
}