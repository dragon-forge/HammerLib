package com.zeitheron.hammercore.utils.base;

public interface IThrowableSupplier<V, E extends Throwable>
{
	V get() throws E;
}