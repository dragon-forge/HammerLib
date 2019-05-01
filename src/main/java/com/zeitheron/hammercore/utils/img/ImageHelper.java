package com.zeitheron.hammercore.utils.img;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImageHelper
{
	public static float getAspectRatio(BufferedImage image)
	{
		return image.getWidth() / (float) image.getHeight();
	}
	
	public static int getWidthByHeightNAR(int height, float aspectRatio)
	{
		return (int) (aspectRatio * height);
	}
	
	public static int getHeightByWidthNAR(int width, float aspectRatio)
	{
		return (int) (width / aspectRatio);
	}
	
	public static BufferedImage scaleDownTo(int maxWidth, int maxHeight, BufferedImage image)
	{
		// Do not downscale it
		if(image.getWidth() <= maxWidth && image.getHeight() <= maxHeight)
			return image;
		
		float aspectRatio = getAspectRatio(image);
		
		int w = Math.min(maxWidth, image.getWidth());
		int wh = getHeightByWidthNAR(maxWidth, aspectRatio);
		
		int h = Math.min(maxHeight, image.getHeight());
		int hw = getWidthByHeightNAR(h, aspectRatio);
		
		BufferedImage target;
		
		if(w >= hw)
			target = new BufferedImage(wh, w, image.getType());
		else
			target = new BufferedImage(hw, h, image.getType());
		
		Graphics2D g = target.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.drawImage(image, 0, 0, target.getWidth(), target.getHeight(), null);
		g.dispose();
		
		return target;
	}
}