package com.pengu.hammercore.common.utils;

import org.apache.logging.log4j.Level;

import net.minecraftforge.fml.common.FMLLog;

public class WrappedLog
{
	public final String owner;
	
	public WrappedLog(String owner)
	{
		this.owner = owner;
	}
	
	public void info(String message, Object... params)
	{
		FMLLog.log(owner, Level.INFO, message, params);
	}
	
	public void warn(String message, Object... params)
	{
		FMLLog.log(owner, Level.WARN, message, params);
	}
	
	public void bigWarn(String message, Object... params)
	{
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		warn("****************************************");
		warn("* " + message, params);
		for(int i = 2; i < 8 && i < trace.length; i++)
			warn("*  at %s%s", trace[i].toString(), i == 7 ? "..." : "");
		warn("****************************************");
	}
	
	public void error(String message, Object... params)
	{
		FMLLog.log(owner, Level.ERROR, message, params);
	}
	
	public void finer(String message, Object... params)
	{
		FMLLog.log(owner, Level.TRACE, message, params);
	}
	
	public void debug(String message, Object... params)
	{
		FMLLog.log(owner, Level.DEBUG, message, params);
	}
}