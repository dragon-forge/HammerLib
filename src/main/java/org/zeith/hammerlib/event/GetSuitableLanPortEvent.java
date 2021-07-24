package org.zeith.hammerlib.event;

import net.minecraftforge.eventbus.api.Event;

public class GetSuitableLanPortEvent
		extends Event
{
	private Integer newPort;

	public GetSuitableLanPortEvent()
	{
	}

	public Integer getNewPort()
	{
		return newPort;
	}

	public void setNewPort(int newPort)
	{
		this.newPort = newPort;
	}
}