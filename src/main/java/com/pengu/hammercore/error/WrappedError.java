package com.pengu.hammercore.error;

public class WrappedError extends Error
{
	public WrappedError(Throwable err)
	{
		super(err);
	}
}