package com.pengu.hammercore.api.mhb;

/**
 * A basic plugin interface. registerCubes is called on server start.
 */
public interface iRayRegistry
{
	public void registerCubes(iRayCubeRegistry cube);
}