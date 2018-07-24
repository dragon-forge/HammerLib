package com.zeitheron.hammercore.utils;

import java.io.UnsupportedEncodingException;

import com.zeitheron.hammercore.lib.zlib.io.IOUtils;

public class UTF8Strings
{
	public static String utf8Str(byte[] data)
	{
		try
		{
			return new String(data, "UTF-8");
		} catch(UnsupportedEncodingException e)
		{
			return new String(data);
		}
	}
	
	public static byte[] utf8Bytes(String str)
	{
		try
		{
			return str.getBytes("UTF-8");
		} catch(UnsupportedEncodingException e)
		{
			return IOUtils.ZERO_ARRAY;
		}
	}
}