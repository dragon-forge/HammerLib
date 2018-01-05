package com.pengu.hammercore.utils;

public enum EnumSide
{
	CLIENT, SERVER, UNIVERSAL;
	
	public boolean isServer()
	{
		return !this.isClient() || this == UNIVERSAL;
	}
	
	public boolean isClient()
	{
		return this == CLIENT || this == UNIVERSAL;
	}
	
	public boolean sideEqual(EnumSide side)
	{
		return side == this || side == UNIVERSAL || this == UNIVERSAL;
	}
}