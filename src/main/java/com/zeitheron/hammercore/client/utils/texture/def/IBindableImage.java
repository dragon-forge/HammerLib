package com.zeitheron.hammercore.client.utils.texture.def;

import java.net.HttpURLConnection;

import com.zeitheron.hammercore.client.utils.IImagePreprocessor;
import com.zeitheron.hammercore.lib.zlib.web.HttpRequest;

public interface IBindableImage
{
	boolean glBind(double timeInTicks, IImagePreprocessor... proc);
	
	boolean isSameURL(String url);
	
	static IBindableImage probe(String url)
	{
		try
		{
			HttpURLConnection urlc = HttpRequest.get(url).userAgent("Hammer Core Mod @ @VERSION@").getConnection();
			
			if(urlc.getContentType().equals("image/gif"))
				return new BindableImageGif(url);
		} catch(Throwable e)
		{
			e.printStackTrace();
		}
		return new BindableImageStt(url);
	}
}