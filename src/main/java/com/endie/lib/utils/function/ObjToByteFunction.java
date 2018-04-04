package com.endie.lib.utils.function;

@FunctionalInterface
public interface ObjToByteFunction<T>
{
	byte applyAsByte(T i);
}