package com.pengu.hammercore.client.utils;

import java.awt.image.BufferedImage;

import net.minecraft.client.renderer.GlStateManager;

public class DynamicSprite
{
	public final int glId = GlStateManager.generateTexture();
	public boolean isMirrored = false;
	
	public void bind()
	{
		GlStateManager.bindTexture(glId);
	}
	
	public void upload(BufferedImage image)
	{
		GLImageManager.loadTexture(image, glId, isMirrored);
	}
	
	public void upload(iPixelGetter image)
	{
		GLImageManager.loadTexture(image, glId, isMirrored);
	}
	
	protected void update()
	{
		
	}
}