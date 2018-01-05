package com.pengu.hammercore.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public final class TextCoding
{
	private TextCoding()
	{
	}
	
	public static String urlEncode(String text)
	{
		String str = null;
		
		try
		{
			str = URLEncoder.encode(text, "UTF-8");
		} catch(UnsupportedEncodingException e)
		{ /* The system should always have the UTF-8 support */
		}
		
		return str;
	}
	
	public static String urlDecode(String text)
	{
		String str = null;
		
		try
		{
			str = URLDecoder.decode(text, "UTF-8");
		} catch(UnsupportedEncodingException e)
		{ /* The system should always have the UTF-8 support */
		}
		
		return str;
	}
}