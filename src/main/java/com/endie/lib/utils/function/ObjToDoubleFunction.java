package com.endie.lib.utils.function;

@FunctionalInterface
public interface ObjToDoubleFunction<T>
{
	double applyAsDouble(T i);
}