package com.pengu.hammercore.hlang;

public interface iHLDefiner
{
	/** Alternative to imports. */
	void defineAccessibleClass(Class cl, String hlName);
	
	void defineVariable(String hlName, Object value);
	
	void defineHook(String hlName, Class... args);
}