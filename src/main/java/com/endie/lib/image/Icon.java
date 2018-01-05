package com.endie.lib.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.pengu.hammercore.common.utils.IOUtils;

public interface Icon
{
	Map<String, Icon> urlIcons = new HashMap<>();
	
	static Icon of(BufferedImage buf)
	{
		if(buf == null)
			return null;
		return () -> buf;
	}
	
	static Icon of(BufferedImage buf, int delay)
	{
		return new Icon()
		{
			@Override
			public BufferedImage buffer()
			{
				return buf;
			}
			
			@Override
			public int delay()
			{
				return delay;
			}
		};
	}
	
	static Icon of(String url)
	{
		/* Prevent resources from being loaded again */
		if(urlIcons.containsKey(url))
			return urlIcons.get(url);
		
		if(url.startsWith("http://") || url.startsWith("https://"))
		{
			try
			{
				Icon ico = of(ImageIO.read(new ByteArrayInputStream(IOUtils.downloadData(url))));
				urlIcons.put(url, ico);
				return ico;
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return of(new File(url));
	}
	
	static Icon of(File file)
	{
		BufferedImage buf;
		try
		{
			buf = ImageIO.read(file);
		} catch(IOException e)
		{
			throw new IOError(e);
		}
		return of(buf);
	}
	
	BufferedImage buffer();
	
	default int delay()
	{
		return 250;
	}
	
	default int width()
	{
		return buffer() == null ? 0 : buffer().getWidth();
	}
	
	default int height()
	{
		return buffer() == null ? 0 : buffer().getHeight();
	}
	
	default Icon round(int arcX, int arcY)
	{
		BufferedImage buf = buffer(), out = new BufferedImage(width(), height(), BufferedImage.TYPE_INT_ARGB /* add
		                                                                                                      * transparency */);
		out.createGraphics().drawImage(buf, 0, 0, null);
		
		BufferedImage round = new BufferedImage(width(), height(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = round.createGraphics();
		g2d.setColor(Color.BLACK);
		g2d.fillRoundRect(0, 0, width(), height(), arcX, arcY);
		
		for(int x = 0; x < width(); ++x)
			for(int y = 0; y < height(); ++y)
				if((round.getRGB(x, y) >> 88 & 0xFF) < 5)
					out.getRaster().setPixel(x, y, new int[] { 255, 255, 255, 0 });
				
		return of(out);
	}
	
	default Icon brighter(int points)
	{
		Icon ic = copy();
		RescaleOp rescaleOp = new RescaleOp(1.2F, points, null);
		rescaleOp.filter(ic.buffer(), ic.buffer());
		return ic;
	}
	
	default Icon copy()
	{
		if(buffer() == null)
			return this;
		BufferedImage buf = buffer(), out = new BufferedImage(width(), height(), buf.getType());
		out.createGraphics().drawImage(buf, 0, 0, null);
		return of(out);
	}
	
	default Icon resize(int width, int height)
	{
		/** Prevent unwanted results */
		if(width() == width && height() == height)
			return this;
		
		BufferedImage buf = buffer(), scaled = new BufferedImage(width, height, buf.getType());
		scaled.createGraphics().drawImage(buf, 0, 0, width, height, null);
		return of(scaled, delay());
	}
	
	default Icon sub(int x, int y, int width, int height)
	{
		return of(buffer().getSubimage(x, y, width, height), delay());
	}
}