package com.zeitheron.hammercore.utils;

import java.lang.reflect.Field;
import java.math.BigInteger;

public class BigIntegerUtils
{
	public static int[] getMagnitude(BigInteger bint)
	{
		try
		{
			Field f = BigInteger.class.getDeclaredField("mag");
			f.setAccessible(true);
			return (int[]) f.get(bint);
		} catch(Throwable err)
		{
		}
		return null;
	}
	
	public static boolean isInt(BigInteger bint)
	{
		if(bint == null)
			return false;
		int[] mag = getMagnitude(bint);
		return (mag == null || mag.length <= 1) && bint.bitLength() <= 31;
	}
}