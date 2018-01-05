package com.pengu.hammercore.hlang;

public interface iHLFunction
{
	Class[] getArgs();
	
	void run(Object... args);
}