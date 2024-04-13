package com.zeitheron.hammercore.utils.java.itf;

import java.util.function.Predicate;

public interface ToFloatFunction<T>
{
	float applyAsFloat(T object);
	
	default ToFloatFunction<T> plus(float amount)
	{
		return t -> amount + applyAsFloat(t);
	}
	
	default ToFloatFunction<T> plus(ToFloatFunction<T> other)
	{
		ToFloatFunction<T> th = this;
		return t -> th.applyAsFloat(t) + other.applyAsFloat(t);
	}
	
	default ToFloatFunction<T> plus(ToFloatFunction<T> other, ToFloatFunction<T>... fun)
	{
		return t ->
		{
			float b = applyAsFloat(t) + other.applyAsFloat(t);
			for(ToFloatFunction<T> fn : fun)
				b += fn.applyAsFloat(t);
			return b;
		};
	}
	
	default ToFloatFunction<T> onlyIf(Predicate<T> filter)
	{
		return f -> filter.test(f) ? applyAsFloat(f) : 0F;
	}
	
	static <T> ToFloatFunction<T> sum(float base, ToFloatFunction<T>... all)
	{
		return t ->
		{
			float sum = base;
			for(ToFloatFunction<T> f : all) sum += f.applyAsFloat(t);
			return sum;
		};
	}
	
	static <T> ToFloatFunction<T> constant(float f)
	{
		return t -> f;
	}
}