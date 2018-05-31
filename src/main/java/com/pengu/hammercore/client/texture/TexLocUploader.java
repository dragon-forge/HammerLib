package com.pengu.hammercore.client.texture;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.pengu.hammercore.HammerCore;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

public class TexLocUploader
{
	public static final List<ResourceLocation> cleanup = new ArrayList<>();
	public static final List<Runnable> cleanCallbacks = new ArrayList<>();
	
	/**
	 * Uploads a {@link BufferedImage} to a {@link ResourceLocation}
	 */
	public static boolean upload(ResourceLocation rl, BufferedImage bi)
	{
		return Minecraft.getMinecraft().getTextureManager().loadTexture(rl, new BufferedTexture(bi));
	}
	
	/**
	 * Uploads a {@link BufferedImage} to a {@link URL} given in string form
	 */
	public static boolean upload(ResourceLocation rl, String url)
	{
		return Minecraft.getMinecraft().getTextureManager().loadTexture(rl, new URLImageTexture(rl, url));
	}
	
	/**
	 * Uploads a {@link BufferedImage} to a {@link ResourceLocation}
	 */
	public static boolean upload(ResourceLocation rl, BufferedImage bi, boolean blur, boolean clamp)
	{
		return Minecraft.getMinecraft().getTextureManager().loadTexture(rl, new BufferedTexture(bi, blur, clamp));
	}
	
	static void cleanupAll()
	{
		HammerCore.LOG.info("Cleaning " + cleanup.size() + " textures from VRAM");
		cleanup.forEach(r -> deleteGlTexture(Minecraft.getMinecraft().getTextureManager().mapTextureObjects.remove(r)));
		cleanCallbacks.forEach(r -> r.run());
		cleanup.clear();
		cleanCallbacks.clear();
	}
	
	public static void cleanupAfterLogoff(ResourceLocation loca, Runnable... runnables)
	{
		cleanup.add(loca);
		cleanCallbacks.addAll(Arrays.asList(runnables));
	}
	
	public static void deleteGlTexture(ITextureObject tex)
	{
		if(tex == null)
			return;
		if(tex instanceof AbstractTexture)
			((AbstractTexture) tex).deleteGlTexture();
		else if(tex.getGlTextureId() != -1)
			TextureUtil.deleteTexture(tex.getGlTextureId());
	}
}