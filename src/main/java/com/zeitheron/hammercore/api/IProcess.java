package com.zeitheron.hammercore.api;

import com.zeitheron.hammercore.HammerCore;

public interface IProcess
{
	void update();
	
	boolean isAlive();
	
	default void onKill()
	{
	}
	
	default void start()
	{
		if(isAlive())
			HammerCore.particleProxy.startProcess(this);
	}
}