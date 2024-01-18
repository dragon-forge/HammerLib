package org.zeith.hammerlib.util.configured.struct.reflection;

/**
 * @deprecated Use {@link org.zeith.hammerlib.util.java.consumers.Consumer3} instead.
 */
@Deprecated(forRemoval = true) // TODO: remove
public interface TriConsumer<A, B, C>
{
	void accept(A a, B b, C c);
}