package org.zeith.hammerlib.util.java.itf;

@FunctionalInterface
public interface IThrowableSupplier<R, E extends Throwable>
{
	R get() throws E;
}