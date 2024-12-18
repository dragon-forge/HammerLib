package org.zeith.hammerlib.util.java.cbq;

@FunctionalInterface
public interface IntCallback4
{
	IntCallback4 NOTHING = (a, b, c, d) -> 0;
	
	int invoke(Object a, Object b, Object c, Object d);
}