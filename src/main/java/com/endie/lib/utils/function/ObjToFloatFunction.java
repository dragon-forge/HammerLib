package com.endie.lib.utils.function;

@FunctionalInterface
public interface ObjToFloatFunction<T>
{
	float applyAsFloat(T i);
}