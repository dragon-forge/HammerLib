package com.zeitheron.hammercore.utils.web;

import java.awt.image.BufferedImage;
import java.net.URL;

import com.zeitheron.hammercore.client.utils.IImagePreprocessor;
import com.zeitheron.hammercore.lib.zlib.utils.PriorityList;
import com.zeitheron.hammercore.lib.zlib.utils.PriorityList.PriorityHolder;

public class URLLocation implements IImagePreprocessor
{
	public final String url;
	private PriorityList<PriorityHolder<IImagePreprocessor>> processors = new PriorityList<>();
	
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
	
	public URLLocation addPreprocessor(IImagePreprocessor proc, int priority)
	{
		if(proc == this)
			return this;
		processors.add(new PriorityHolder<>(proc, priority));
		return this;
	}
	
	@Override
	public BufferedImage process(BufferedImage image)
	{
		for(int i = 0; i < processors.size(); ++i)
			processors.get(i).val.process(image);
		return image;
	}
}