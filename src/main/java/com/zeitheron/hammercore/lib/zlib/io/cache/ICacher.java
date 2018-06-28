package com.zeitheron.hammercore.lib.zlib.io.cache;

import java.io.InputStream;
import java.io.OutputStream;

public interface ICacher
{
	OutputStream put(String url);
	
	InputStream pull(String url);
	
	boolean isActuallyWorking();
	
	default boolean preventLiveConnection(String url)
	{
		return false;
	}
}