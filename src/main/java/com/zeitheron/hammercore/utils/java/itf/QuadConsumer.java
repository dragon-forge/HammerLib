package com.zeitheron.hammercore.utils.java.itf;

@FunctionalInterface
public interface QuadConsumer<A, B, C, D>
{
	void accept(A a, B b, C c, D d);
}