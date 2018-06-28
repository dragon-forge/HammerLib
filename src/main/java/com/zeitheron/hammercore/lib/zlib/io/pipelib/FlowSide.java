package com.zeitheron.hammercore.lib.zlib.io.pipelib;

public enum FlowSide
{
	CLIENT,
	SERVER;
	
	public boolean isServer()
	{
		return this == SERVER;
	}
	
	public boolean isClient()
	{
		return this == CLIENT;
	}
}