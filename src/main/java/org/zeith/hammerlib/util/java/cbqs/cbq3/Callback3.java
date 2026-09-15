package org.zeith.hammerlib.util.java.cbqs.cbq3;

@FunctionalInterface
public interface Callback3
{
	Callback3 NOTHING = (a, b, c) -> null;
	
	Object invoke(Object a, Object b, Object c);
}