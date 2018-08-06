package com.zeitheron.hammercore.api.mhb;

/**
 * A basic plugin interface. registerCubes is called on server start.
 * Subscribe to the event by annotating the class with @{@link RaytracePlugin}
 */
public interface IRayRegistry
{
	/**
	 * Called when the world is loaded.
	 */
	public void registerCubes(IRayCubeRegistry cube);
}