package com.zeitheron.hammercore.lib.zlib.utils;

import java.util.LinkedList;

public class TaskedThread extends Thread
{
	private final LinkedList<Runnable> tasks = new LinkedList<>();
	private boolean running = true;
	
	{
		start();
	}
	
	public final void addTask(Runnable task)
	{
		tasks.addLast(task);
		synchronized(tasks)
		{
			tasks.notifyAll();
		}
	}
	
	@Override
	public void interrupt()
	{
		running = false;
		super.interrupt();
	}
	
	@Override
	public void run()
	{
		while(running)
		{
			synchronized(tasks)
			{
				try
				{
					tasks.wait();
				} catch(InterruptedException e)
				{
				}
			}
			
			while(!tasks.isEmpty())
				tasks.pop().run();
		}
	}
}