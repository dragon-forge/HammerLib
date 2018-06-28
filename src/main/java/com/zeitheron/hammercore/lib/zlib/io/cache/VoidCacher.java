package com.zeitheron.hammercore.lib.zlib.io.cache;

import java.io.InputStream;
import java.io.OutputStream;

import com.zeitheron.hammercore.lib.zlib.io.VoidInputStream;
import com.zeitheron.hammercore.lib.zlib.io.VoidOutputStream;

public final class VoidCacher implements ICacher
{
	@Override
	public OutputStream put(String url)
	{
		return new VoidOutputStream();
	}
	
	@Override
	public InputStream pull(String url)
	{
		return new VoidInputStream();
	}
	
	@Override
	public boolean isActuallyWorking()
	{
		return true;
	}
}