package org.zeith.hammerlib.util.java.cbqs.cbq4;

@FunctionalInterface
public interface DoubleCallback4
{
	DoubleCallback4 NOTHING = (a, b, c, d) -> 0;
	
	double invoke(Object a, Object b, Object c, Object d);
}