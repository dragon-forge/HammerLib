package com.zeitheron.hammercore.api;

/**
 * Simple logging interface for external APIs
 */
public interface ILog
{
	public void info(String message, Object... params);
	
	public void warn(String message, Object... params);
	
	public void error(String message, Object... params);
	
	public void finer(String message, Object... params);
}