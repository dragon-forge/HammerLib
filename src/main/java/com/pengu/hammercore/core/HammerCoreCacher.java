package com.pengu.hammercore.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.endie.lib.io.VoidInputStream;
import com.endie.lib.io.VoidOutputStream;
import com.endie.lib.io.cache.iCacher;
import com.pengu.hammercore.common.utils.MD5;

public class HammerCoreCacher implements iCacher
{
	public static File cacheDir = new File("HammerCore/web_cache");
	
	static
	{
		if(!cacheDir.isDirectory())
			cacheDir.mkdirs();
	}
	
	@Override
	public boolean isActuallyWorking()
	{
		return true;
	}
	
	@Override
	public boolean preventLiveConnection(String url)
	{
		return false;
	}
	
	@Override
	public InputStream pull(String url)
	{
		File f = new File(cacheDir, MD5.encrypt(url));
		try
		{
			return new FileInputStream(f);
		} catch(Throwable err)
		{
		}
		return new VoidInputStream();
	}
	
	@Override
	public OutputStream put(String url)
	{
		File f = new File(cacheDir, MD5.encrypt(url));
		try
		{
			return new FileOutputStream(f);
		} catch(Throwable err)
		{
		}
		return new VoidOutputStream();
	}
}