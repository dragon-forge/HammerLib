package com.zeitheron.hammercore.lib.zlib.error;

public class ChannelMismatchException extends Error
{
	private static final long serialVersionUID = -3911872696663189491L;
	
	public ChannelMismatchException(String our, String remote)
	{
		super("Channel mismatch detected! Current Channel: " + our + "; Resolved Channel: " + remote);
	}
}
