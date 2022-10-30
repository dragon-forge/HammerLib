package org.zeith.hammerlib.util.java.itf;

@FunctionalInterface
@Deprecated(forRemoval = true) // TODO: Delete this file
public interface QuadConsumer<A, B, C, D>
{
	void accept(A a, B b, C c, D d);
}