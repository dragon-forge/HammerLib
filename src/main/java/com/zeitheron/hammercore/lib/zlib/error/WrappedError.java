package com.zeitheron.hammercore.lib.zlib.error;

public class WrappedError extends Error
{
	private static final long serialVersionUID = -2137728937949425263L;

	public WrappedError(Throwable err)
	{
		super(err);
	}
}