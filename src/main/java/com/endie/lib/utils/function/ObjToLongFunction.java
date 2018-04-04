package com.endie.lib.utils.function;

@FunctionalInterface
public interface ObjToLongFunction<T>
{
	long applyAsLong(T i);
}