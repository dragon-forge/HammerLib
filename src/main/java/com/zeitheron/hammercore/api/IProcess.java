package com.zeitheron.hammercore.api;

import com.zeitheron.hammercore.HammerCore;

/**
 * The process API that may run each server/client tick.
 */
public interface IProcess
{
	/**
	 * Called each tick on the side that it was started.
	 */
	void update();
	
	/**
	 * Checks if this process can still tick. IF return value is false, the
	 * process will be killed, and {@link #onKill()} will be called.
	 */
	boolean isAlive();
	
	/**
	 * Called once after this process becomes no longer alive.
	 */
	default void onKill()
	{
	}
	
	/**
	 * Starts this process. The side that this method is called will be used to
	 * tick the process further.
	 */
	default void start()
	{
		if(isAlive())
			HammerCore.particleProxy.startProcess(this);
	}
}