package com.zeitheron.hammercore.lib.zlib.error;

public class NoDefaultConstructorsExceptions extends Error
{
	private static final long serialVersionUID = -8838018343092284626L;
	
	public NoDefaultConstructorsExceptions(Class<?> cls)
	{
		super("No default constructors found for class " + cls.getName());
	}
	
	public NoDefaultConstructorsExceptions(Class<?> cls, Throwable err)
	{
		super("No default constructors found for class " + cls.getName(), err);
	}
}
