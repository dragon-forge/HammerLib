package com.pengu.hammercore.core.img;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public interface ImageFilter
{
	ImageFilter BLACK_AND_WHITE = img ->
	{
		for(int x = 0; x < img.getWidth(); ++x)
			for(int y = 0; y < img.getHeight(); ++y)
			{
				int rgb = img.getRGB(x, y);
				float r = (rgb >> 16 & 0xFF) / 255F, g = (rgb >> 8 & 0xFF) / 255F, b = (rgb & 0xFF) / 255F;
				int bri = (int) (r * g * b * 255F);
				rgb = 255 << 24 | bri << 16 | bri << 8 | bri;
				img.setRGB(x, y, rgb);
			}
		
		return img;
	};
	
	ImageFilter BRIGHTEN = img ->
	{
		RescaleOp rescaleOp = new RescaleOp(1.2F, 15, null);
		rescaleOp.filter(img, img);
		return img;
	};
	
	default BufferedImage applyToCopy(BufferedImage img)
	{
		BufferedImage i = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		i.createGraphics().drawImage(img, 0, 0, null);
		return apply(i);
	}
	
	BufferedImage apply(BufferedImage bi);
}