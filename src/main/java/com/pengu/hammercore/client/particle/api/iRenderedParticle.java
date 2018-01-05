package com.pengu.hammercore.client.particle.api;

public interface iRenderedParticle
{
	default void doRenderParticle(double x, double y, double z, float partialTicks)
	{
		
	}
	
	default void doRenderParticle(double x, double y, double z, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		doRenderParticle(x, y, z, partialTicks);
	}
	
	default boolean isRendered()
	{
		return false;
	}
	
	default void setRendered()
	{
		
	}
}