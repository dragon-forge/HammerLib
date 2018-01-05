package com.endie.lib.utils.function;

@FunctionalInterface
public interface ObjToIntFunction<T>
{
	int applyAsInt(T i);
}