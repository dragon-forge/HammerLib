package org.zeith.hammerlib.util.java.cbqs.cbq3;

@FunctionalInterface
public interface DoubleCallback3
{
	DoubleCallback3 NOTHING = (a, b, c) -> 0;
	
	double invoke(Object a, Object b, Object c);
}