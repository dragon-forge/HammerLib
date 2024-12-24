package org.zeith.hammerlib.util.java.cbqs.cbq3;

@FunctionalInterface
public interface IntCallback3
{
	IntCallback3 NOTHING = (a, b, c) -> 0;
	
	int invoke(Object a, Object b, Object c);
}