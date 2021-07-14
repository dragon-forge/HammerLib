package org.zeith.hammerlib.util.java.itf;

@FunctionalInterface
public interface IThrowableConsumer<R, E extends Throwable>
{
	void get(R item) throws E;
}