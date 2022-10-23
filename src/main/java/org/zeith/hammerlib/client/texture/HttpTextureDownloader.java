package org.zeith.hammerlib.client.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;

public class HttpTextureDownloader
{
	public static AbstractTexture create(ResourceLocation texturePath, String url)
	{
		return create(texturePath, url, null);
	}
	
	public static AbstractTexture create(ResourceLocation texturePath, String url, Runnable onDownloadComplete)
	{
		HttpTexture tex = new HttpTexture(null, url, DefaultPlayerSkin.getDefaultSkin(), false, onDownloadComplete);
		Minecraft.getInstance().textureManager.register(texturePath, tex);
		return tex;
	}
}