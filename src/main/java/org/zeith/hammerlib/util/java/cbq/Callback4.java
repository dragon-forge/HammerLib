package org.zeith.hammerlib.util.java.cbq;

@FunctionalInterface
public interface Callback4
{
	Callback4 NOTHING = (a, b, c, d) -> null;
	
	Object invoke(Object a, Object b, Object c, Object d);
}