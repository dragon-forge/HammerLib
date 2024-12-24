package org.zeith.hammerlib.util.java.cbqs.cbq4;

@FunctionalInterface
public interface BoolCallback4
{
	BoolCallback4 NOTHING = (a, b, c, d) -> false;
	
	boolean invoke(Object a, Object b, Object c, Object d);
}