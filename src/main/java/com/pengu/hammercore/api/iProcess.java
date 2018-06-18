package com.pengu.hammercore.api;

import com.pengu.hammercore.HammerCore;

public interface iProcess
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