package org.zeith.hammerlib.event;

import net.minecraftforge.eventbus.api.Event;

public class GetSuitableLanPortEvent
		extends Event
{
	private final int originalPort;
	private int newPort;

	public GetSuitableLanPortEvent(int port)
	{
		this.newPort = this.originalPort = port;
	}

	public int getOriginalPort()
	{
		return originalPort;
	}

	public int getNewPort()
	{
		return newPort;
	}

	public void setNewPort(int newPort)
	{
		this.newPort = newPort;
	}
}