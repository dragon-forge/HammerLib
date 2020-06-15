package com.zeitheron.hammercore.utils.java.itf;

@FunctionalInterface
public interface IThrowableSupplier<R, E extends Throwable>
{
	R get() throws E;
}