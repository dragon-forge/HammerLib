package com.pengu.hammercore.client.texture;

import java.awt.image.BufferedImage;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class TexLocUploader
{
	/**
	 * Uploads a {@link BufferedImage} to a {@link ResourceLocation}
	 */
	public static boolean upload(ResourceLocation rl, BufferedImage bi)
	{
		return Minecraft.getMinecraft().getTextureManager().loadTexture(rl, new BufferedTexture(bi));
	}
	
	/**
	 * Uploads a {@link BufferedImage} to a {@link ResourceLocation}
	 */
	public static boolean upload(ResourceLocation rl, BufferedImage bi, boolean blur, boolean clamp)
	{
		return Minecraft.getMinecraft().getTextureManager().loadTexture(rl, new BufferedTexture(bi, blur, clamp));
	}
}