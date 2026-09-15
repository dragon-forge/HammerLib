package org.zeith.hammerlib.util.java.cbqs.cbq3;

@FunctionalInterface
public interface BoolCallback3
{
	BoolCallback3 NOTHING = (a, b, c) -> false;
	
	boolean invoke(Object a, Object b, Object c);
}