package org.zeith.hammerlib.util.java.cbqs.cbq4;

@FunctionalInterface
public interface Callback4
{
	Callback4 NOTHING = (a, b, c, d) -> null;
	
	Object invoke(Object a, Object b, Object c, Object d);
}