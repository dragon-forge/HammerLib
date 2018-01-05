package com.pengu.hammercore.api;

import org.apache.logging.log4j.Level;

import net.minecraftforge.fml.common.FMLLog;

/**
 * Implementation for {@link iLog}. Used in
 * {@link iHammerCoreAPI#init(iLog, String)}
 */
public class WrappedFMLLog implements iLog
{
	public final String owner;
	
	public WrappedFMLLog(String owner)
	{
		this.owner = owner;
	}
	
	@Override
	public void info(String message, Object... params)
	{
		FMLLog.log("HammerCore|" + owner, Level.INFO, message, params);
	}
	
	@Override
	public void warn(String message, Object... params)
	{
		FMLLog.log("HammerCore|" + owner, Level.WARN, message, params);
	}
	
	@Override
	public void error(String message, Object... params)
	{
		FMLLog.log("HammerCore|" + owner, Level.ERROR, message, params);
	}
	
	@Override
	public void finer(String message, Object... params)
	{
		FMLLog.log("HammerCore|" + owner, Level.TRACE, message, params);
	}
}