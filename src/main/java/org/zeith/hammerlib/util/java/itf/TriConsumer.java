package org.zeith.hammerlib.util.java.itf;

@FunctionalInterface
@Deprecated(forRemoval = true) // TODO: Delete this file
public interface TriConsumer<A, B, C>
{
	void accept(A a, B b, C c);
}