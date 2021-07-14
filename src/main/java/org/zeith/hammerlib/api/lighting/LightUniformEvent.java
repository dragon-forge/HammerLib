package org.zeith.hammerlib.api.lighting;

import net.minecraftforge.eventbus.api.Event;

import java.util.List;

/**
 * Called by a lighting mod when the lights are set up
 */
public class LightUniformEvent
		extends Event
{
	final List<ColoredLight> lights;

	public LightUniformEvent(List<ColoredLight> lights)
	{
		this.lights = lights;
	}

	public List<ColoredLight> getLights()
	{
		return lights;
	}
}