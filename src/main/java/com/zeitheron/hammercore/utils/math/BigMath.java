package com.zeitheron.hammercore.utils.math;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigMath
{
	// Big Decimals
	
	public static final BigDecimal PI = new BigDecimal(22D / 7);
	
	public static BigDecimal max(BigDecimal a, BigDecimal b)
	{
		if(a == null || b == null)
			return a != null ? b : a;
		return a.max(b);
	}
	
	public static BigDecimal min(BigDecimal a, BigDecimal b)
	{
		if(a == null || b == null)
			return a != null ? b : a;
		return a.min(b);
	}
	
	public static BigDecimal pow(BigDecimal d, int pow)
	{
		return d.pow(pow);
	}
	
	public static boolean isAGreaterThenB(BigDecimal a, BigDecimal b, boolean strict)
	{
		return (a.equals(b) && !strict) || (!a.equals(b) && a.max(b).equals(a));
	}
	
	public static boolean isALesserThenB(BigDecimal a, BigDecimal b, boolean strict)
	{
		return (a.equals(b) && !strict) || (!a.equals(b) && a.min(b).equals(a));
	}
	
	// Big Integers
	
	public static final BigInteger INT_MAX_VALUE = new BigInteger(Integer.MAX_VALUE + "");
	public static final BigInteger INT_MIN_VALUE = new BigInteger(Integer.MIN_VALUE + "");
	
	public static BigInteger max(BigInteger a, BigInteger b)
	{
		if(a == null || b == null)
			return a != null ? b : a;
		return a.max(b);
	}
	
	public static BigInteger min(BigInteger a, BigInteger b)
	{
		if(a == null || b == null)
			return a != null ? b : a;
		return a.min(b);
	}
	
	public static BigInteger pow(BigInteger d, int pow)
	{
		return d.pow(pow);
	}
	
	public static boolean isAGreaterThenB(BigInteger a, BigInteger b, boolean strict)
	{
		return (a.equals(b) && !strict) || (!a.equals(b) && a.max(b).equals(a));
	}
	
	public static boolean isALesserThenB(BigInteger a, BigInteger b, boolean strict)
	{
		return (a.equals(b) && !strict) || (!a.equals(b) && a.min(b).equals(a));
	}
	
	public static int trimToInt(BigInteger bi)
	{
		return min(INT_MAX_VALUE, max(INT_MIN_VALUE, bi)).intValue();
	}
}