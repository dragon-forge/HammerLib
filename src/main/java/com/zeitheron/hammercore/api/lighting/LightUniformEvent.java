package com.zeitheron.hammercore.api.lighting;

import java.util.List;

import net.minecraftforge.fml.common.eventhandler.Event;

public class LightUniformEvent extends Event
{
	final List<ColoredLight> lights;
	
	public LightUniformEvent(List<ColoredLight> lights)
	{
		super();
		this.lights = lights;
	}
	
	public List<ColoredLight> getLights()
	{
		return lights;
	}
	
	@Override
	public boolean isCancelable()
	{
		return false;
	}
}