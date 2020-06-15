package com.zeitheron.hammercore.utils.java.itf;

@FunctionalInterface
public interface TriConsumer<A,B,C>
{
	void accept(A a, B b, C c);
}