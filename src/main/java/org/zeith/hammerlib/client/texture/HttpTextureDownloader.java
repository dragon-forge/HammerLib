package org.zeith.hammerlib.client.texture;

import lombok.var;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

public class HttpTextureDownloader
{
	public static Texture create(ResourceLocation texturePath, String url)
	{
		return create(texturePath, url, null);
	}
	
	public static Texture create(ResourceLocation texturePath, String url, Runnable onDownloadComplete)
	{
		var tex = new DownloadingTexture(null, url, DefaultPlayerSkin.getDefaultSkin(), false, onDownloadComplete);
		Minecraft.getInstance().textureManager.register(texturePath, tex);
		return tex;
	}
}