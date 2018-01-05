package com.pengu.hammercore.api;

/**
 * Simple loging interface
 */
public interface iLog
{
	public void info(String message, Object... params);
	
	public void warn(String message, Object... params);
	
	public void error(String message, Object... params);
	
	public void finer(String message, Object... params);
}