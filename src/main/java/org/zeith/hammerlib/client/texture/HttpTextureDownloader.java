package org.zeith.hammerlib.client.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;

public class HttpTextureDownloader
{
	public static AbstractTexture create(ResourceLocation texturePath, String url)
	{
		return create(texturePath, url, null);
	}
	
	public static AbstractTexture create(ResourceLocation texturePath, String url, Runnable onDownloadComplete)
	{
		var tex = Minecraft.getInstance().textureManager.getTexture(texturePath, null);
		if(tex != null) return tex;
		tex = new HttpTextureWithHeaders(null, url, texturePath, false, onDownloadComplete);
		Minecraft.getInstance().textureManager.register(texturePath, tex);
		return tex;
	}
}