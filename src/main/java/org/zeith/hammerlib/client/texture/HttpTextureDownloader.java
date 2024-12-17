package org.zeith.hammerlib.client.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.util.java.Once;

import java.util.concurrent.atomic.AtomicBoolean;

public class HttpTextureDownloader
{
	@NotNull
	public static AbstractTexture create(ResourceLocation texturePath, String url)
	{
		return create(texturePath, url, null);
	}
	
	@NotNull
	public static AbstractTexture create(ResourceLocation texturePath, String url, Runnable onDownloadComplete)
	{
		Once completion = Once.run(onDownloadComplete);
		
		var reg = Minecraft.getInstance().textureManager.byPath.get(texturePath);
		if(reg instanceof FutureTexture ft && !ft.isStale())
		{
			completion.call();
			return ft;
		}
		
		FutureTexture ft = new FutureTexture(texturePath);
		Minecraft.getInstance().textureManager.register(texturePath, ft);
		
		HttpTextureWithHeaders.readImage(null, url, image ->
				{
					ft.updateImage(image);
					completion.call();
				}
		);
		
		return ft;
	}
}