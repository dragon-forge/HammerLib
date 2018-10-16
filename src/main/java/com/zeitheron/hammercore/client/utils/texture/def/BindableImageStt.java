package com.zeitheron.hammercore.client.utils.texture.def;

import java.util.Objects;

import com.zeitheron.hammercore.client.utils.IImagePreprocessor;

public class BindableImageStt implements IBindableImage
{
	public final String url;
	
	public BindableImageStt(String url)
	{
		this.url = url;
	}
	
	@Override
	public boolean glBind(double timeInTicks, IImagePreprocessor... proc)
	{
		return ImagePuller.bind(url, proc);
	}
	
	@Override
	public boolean isSameURL(String url)
	{
		return Objects.equals(url, this.url);
	}
}