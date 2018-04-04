package com.endie.lib.utils.function;

@FunctionalInterface
public interface ObjToShortFunction<T>
{
	short applyAsShort(T i);
}