package org.zeith.hammerlib.client.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
public class FutureTexture
		extends ReloadableTexture
{
	public final UUID ogID = UUID.randomUUID();
	public UUID curID = ogID;
	
	public boolean isLoaded;
	
	@Nullable
	protected NativeImage pixels;
	
	public FutureTexture(ResourceLocation resourceId)
	{
		super(resourceId);
	}
	
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
			try
			{
				apply(loadContents(null));
				isLoaded = true;
			} catch(IOException e)
			{
				log.error("Failed to load future texture.", e);
			}
		} else
		{
			log.warn("Trying to upload future texture {} with no pixel data", this.getId());
		}
	}
	
	public boolean isStale()
	{
		return !Objects.equals(curID, ogID);
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
	
	@Override
	public TextureContents loadContents(@Nullable ResourceManager resourceManager)
			throws IOException
	{
		curID = UUID.randomUUID();
		if(pixels == null) throw new FileNotFoundException();
		return new TextureContents(pixels, null);
	}
}