package org.zeith.hammerlib.api.lighting;

import net.neoforged.bus.api.*;

/**
 * This event is fired on {@link org.zeith.hammerlib.HammerLib#EVENT_BUS}
 */
public class HandleLightOverrideEvent
		extends Event
{
	private final Object object;
	private final float partialTime;
	private final ColoredLight light;
	private ColoredLight newLight;
	
	public HandleLightOverrideEvent(Object object, float partialTime, ColoredLight light)
	{
		this.object = object;
		this.partialTime = partialTime;
		this.light = this.newLight = light;
	}
	
	public float getPartialTime()
	{
		return partialTime;
	}
	
	public ColoredLight getLight()
	{
		return light;
	}
	
	public void setNewLight(ColoredLight newLight)
	{
		this.newLight = newLight;
	}
	
	public ColoredLight getNewLight()
	{
		return newLight;
	}
}