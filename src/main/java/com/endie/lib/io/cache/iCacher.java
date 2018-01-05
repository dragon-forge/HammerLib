package com.endie.lib.io.cache;

import java.io.InputStream;
import java.io.OutputStream;

public interface iCacher
{
	OutputStream put(String url);
	
	InputStream pull(String url);
	
	boolean isActuallyWorking();
	
	default boolean preventLiveConnection(String url)
	{
		return false;
	}
}