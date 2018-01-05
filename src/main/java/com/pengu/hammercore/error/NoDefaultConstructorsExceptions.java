package com.pengu.hammercore.error;

public class NoDefaultConstructorsExceptions extends Error
{
	public NoDefaultConstructorsExceptions(Class<?> cls)
	{
		super("No default constructors found for class " + cls.getName());
	}
	
	public NoDefaultConstructorsExceptions(Class<?> cls, Throwable err)
	{
		super("No default constructors found for class " + cls.getName(), err);
	}
}