package com.zeitheron.hammercore.utils;

public class NPEUtils
{
	public static <T> void checkNotNull(T t, String field)
	{
		if(t == null)
			throw new NullPointerException(field);
	}
	
	/**
	 * Throws a runtime error. use in constructor to prevent creating a new
	 * instance of your class
	 */
	public static void noInstancesError()
	{
		StackTraceElement[] elems = Thread.currentThread().getStackTrace();
		throw new RuntimeException("No " + elems[2].getClassName() + " instances for you, cheater!");
	}
}