package com.zeitheron.hammercore.lib.zlib.utils;

public class Threading
{
	public static boolean isRunning(Thread thr)
	{
		return thr != null && thr.isAlive();
	}
	
	public static Thread createAndStart(Runnable run)
	{
		Thread thr = new Thread(run);
		thr.start();
		return thr;
	}
	
	public static Thread createAndStart(String name, Runnable run)
	{
		Thread thr = new Thread(run);
		thr.setName(name);
		thr.start();
		return thr;
	}
	
	public static void sleep(long ms)
	{
		try
		{
			Thread.sleep(ms);
		} catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}