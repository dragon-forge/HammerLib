package com.zeitheron.hammercore.internal.variables;

import net.minecraftforge.fml.relauncher.Side;

public enum NetworkDirection
{
	CLIENT_TO_SERVER(false, true),
	SERVER_TO_CLIENT(true, false),
	BOTH(true, true),
	NONE(false, false);

	private final boolean s2c, c2s;

	NetworkDirection(boolean s2c, boolean c2s)
	{
		this.s2c = s2c;
		this.c2s = c2s;
	}

	public boolean serverToClient()
	{
		return s2c;
	}

	public boolean clientToServer()
	{
		return c2s;
	}

	public boolean allowedTo(Side side)
	{
		return (side == Side.CLIENT && s2c) || (side == Side.SERVER && c2s);
	}

	public boolean allowedFrom(Side side)
	{
		return (side == Side.SERVER && s2c) || (side == Side.CLIENT && c2s);
	}
}