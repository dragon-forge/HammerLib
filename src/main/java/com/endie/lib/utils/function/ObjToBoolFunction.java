package com.endie.lib.utils.function;

@FunctionalInterface
public interface ObjToBoolFunction<T>
{
	boolean applyAsBool(T i);
}