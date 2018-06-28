package com.zeitheron.hammercore.utils;

import java.util.UUID;

public class StringHelper
{
	public static String insertChar(String str, int at, char c)
	{
		return str.substring(0, at) + c + str.substring(at);
	}
	
	public static String trimUUID(UUID uuid)
	{
		return uuid.toString().replaceAll("-", "");
	}
	
	public static UUID untrimUUID(String uuid)
	{
		String untrimmed = insertChar(uuid, 8, '-');
		untrimmed = insertChar(untrimmed, 13, '-');
		untrimmed = insertChar(untrimmed, 18, '-');
		untrimmed = insertChar(untrimmed, 23, '-');
		return UUID.fromString(untrimmed);
	}
}