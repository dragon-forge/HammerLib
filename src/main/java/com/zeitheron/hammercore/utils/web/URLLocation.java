package com.zeitheron.hammercore.utils.web;

import java.net.URL;

public class URLLocation
{
	public final String url;
	
	public URLLocation(String url)
	{
		try
		{
			new URL(url);
		} catch(Throwable err)
		{
			throw new RuntimeException("Invalid URL format!", err);
		}
		
		this.url = url;
	}
}