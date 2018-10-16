package com.zeitheron.hammercore.client.utils.texture.def;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import com.zeitheron.hammercore.client.utils.IImagePreprocessor;
import com.zeitheron.hammercore.client.utils.UtilsFX;
import com.zeitheron.hammercore.client.utils.texture.gif.GLGifInfo;
import com.zeitheron.hammercore.client.utils.texture.gif.GifDecoder;
import com.zeitheron.hammercore.lib.zlib.utils.Threading;
import com.zeitheron.hammercore.lib.zlib.web.HttpRequest;

public class BindableImageGif implements IBindableImage
{
	public final String url;
	public GLGifInfo info;
	
	private boolean init = false;
	
	public BindableImageGif(String url)
	{
		this.url = url;
	}
	
	private void init(IImagePreprocessor... proc)
	{
		init = true;
		Threading.createAndStart("DwnGIF$" + url, () ->
		{
			try(InputStream in = HttpRequest.get(url).userAgent("HammerCore/@VERSION@").stream())
			{
				info = GifDecoder.decodeGifIntoVideoMem(in, proc);
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		});
	}
	
	@Override
	public boolean glBind(double timeInTicks, IImagePreprocessor... proc)
	{
		if(!init)
			init(proc);
		if(info == null)
		{
			UtilsFX.bindTexture(ImagePuller.nosignal_gl);
			return true;
		}
		float mod = 10F;
		
		long milliTime = (long) (timeInTicks * 50D);
		if(milliTime < 0L)
			milliTime = -milliTime;
		int frame = info.getFrameFromMS(milliTime);
		
		if(frame < 0)
		{
			UtilsFX.bindTexture(ImagePuller.nosignal_gl);
			return true;
		}
		
		UtilsFX.bindTexture(info.tex[frame % info.tex.length]);
		return true;
	}
	
	@Override
	public boolean isSameURL(String url)
	{
		return Objects.equals(url, this.url);
	}
}