package com.pengu.hammercore.error;

public class Errors
{
	public static void propagate(Throwable err)
	{
		throw new WrappedError(err);
	}
}