package org.zeith.hammerlib.client.render.item;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

public class RecenterFilter
{
	public static CompletableFuture<BufferedImage> recenter(BufferedImage image)
	{
		return CompletableFuture.supplyAsync(() ->
		{
			BufferedImage canvas = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D gfx = canvas.createGraphics();
			renderInCenter(gfx, 0, 0, image.getWidth(), image.getHeight(), image);
			gfx.dispose();
			return canvas;
		});
	}
	
	static void renderInCenter(Graphics2D gfx, int x, int y, int width, int height, BufferedImage img)
	{
		Rectangle infoBounds = getUsefulTextureBounds(img);
		img = img.getSubimage(infoBounds.x, infoBounds.y, infoBounds.width, infoBounds.height);
		int ctnW = img.getWidth() / 2, ctnH = img.getHeight() / 2;
		gfx.drawImage(img, width / 2 - ctnW, height / 2 - ctnH, null);
	}
	
	static Rectangle getUsefulTextureBounds(BufferedImage img)
	{
		Rectangle rect = new Rectangle();
		
		rect.x = rect.y = Integer.MAX_VALUE;
		rect.width = rect.height = 0;
		
		for(int x = 0; x < img.getWidth(); ++x)
			for(int y = 0; y < img.getHeight(); ++y)
				if(!isPixelTransparent(img, x, y))
				{
					rect.x = Math.min(rect.x, x);
					rect.y = Math.min(rect.y, y);
					
					rect.width = Math.max(rect.width, x);
					rect.height = Math.max(rect.height, y);
				}
		
		rect.width -= rect.x - 1;
		rect.height -= rect.y - 1;
		
		return rect;
	}
	
	static boolean isPixelTransparent(BufferedImage img, int x, int y)
	{
		return ((img.getRGB(x, y) >> 24) & 0xFF) < 10;
	}
}