package org.zeith.hammerlib.client.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;

import javax.annotation.Nullable;

@Slf4j
public class FutureTexture
		extends AbstractTexture
{
	@Nullable
	protected NativeImage pixels;
	
	public void updateImage(NativeImage newImage)
	{
		setPixels(newImage);
		
		Minecraft.getInstance().execute(() ->
		{
			if(!RenderSystem.isOnRenderThread())
			{
				RenderSystem.recordRenderCall(this::upload);
			} else
			{
				upload();
			}
		});
	}
	
	public void upload()
	{
		if(this.pixels != null)
		{
			this.bind();
			this.pixels.upload(0, 0, 0, false);
		} else
		{
			log.warn("Trying to upload future texture {} with no ", this.getId());
		}
	}
	
	public void setPixels(NativeImage pPixels)
	{
		if(this.pixels != null)
		{
			this.pixels.close();
		}
		
		this.pixels = pPixels;
	}
	
	@Nullable
	public NativeImage getPixels()
	{
		return this.pixels;
	}
}