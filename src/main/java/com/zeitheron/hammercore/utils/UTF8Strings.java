package com.zeitheron.hammercore.utils;

import java.nio.charset.StandardCharsets;

public class UTF8Strings
{
	public static String utf8Str(byte[] data)
	{
		return new String(data, StandardCharsets.UTF_8);
	}

	public static byte[] utf8Bytes(String str)
	{
		return str.getBytes(StandardCharsets.UTF_8);
	}
}