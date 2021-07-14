package org.zeith.hammerlib.api.lighting;

import net.minecraftforge.eventbus.api.GenericEvent;

public class HandleLightOverrideEvent<T>
		extends GenericEvent<T>
{
	private final T object;
	private final float partialTime;
	private final ColoredLight light;
	private ColoredLight newLight;

	public HandleLightOverrideEvent(T object, float partialTime, ColoredLight light)
	{
		super((Class<T>) object.getClass());
		this.object = object;
		this.partialTime = partialTime;
		this.light = this.newLight = light;
	}

	public T getObject()
	{
		return object;
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