package org.zeith.hammerlib.client.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.net.HttpRequest;
import org.zeith.hammerlib.util.mcf.ModHelper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HttpTextureWithHeaders
		extends HttpTexture
{
	public static final Map<String, String> EXTRA_HEADERS = new HashMap<>();
	
	static
	{
		EXTRA_HEADERS.put(HttpRequest.HEADER_USER_AGENT, "HammerLib-based HTTP Texture Downloader (" + ModHelper.getModVersion(HLConstants.MOD_ID) + ")");
	}
	
	public HttpTextureWithHeaders(@Nullable File cacheFile, String url, ResourceLocation texturePath, boolean convertMcSkin, @Nullable Runnable onDownloaded)
	{
		super(cacheFile, url, texturePath, convertMcSkin, onDownloaded);
	}
	
	@Override
	public void bind()
	{
		super.bind();
		RenderSystem.setShaderTexture(0, location);
	}
	
	public void loadTexture(ResourceManager p_118135_) throws IOException
	{
		SimpleTexture.TextureImage tex = this.getTextureImage(p_118135_);
		tex.throwIfError();
		
		NativeImage nativeimage = tex.getImage();
		
		if(!RenderSystem.isOnRenderThreadOrInit())
		{
			RenderSystem.recordRenderCall(() ->
			{
				upload(nativeimage);
			});
		} else
		{
			upload(nativeimage);
		}
	}
	
	@Override
	public void load(ResourceManager resources) throws IOException
	{
		Minecraft.getInstance().execute(() ->
		{
			if(!this.uploaded)
			{
				try
				{
					loadTexture(resources);
				} catch(IOException ioexception)
				{
					LOGGER.warn("Failed to load texture: {}", this.location, ioexception);
				}
				
				this.uploaded = true;
			}
		});
		
		if(this.future == null)
		{
			NativeImage nativeimage;
			if(this.file != null && this.file.isFile())
			{
				LOGGER.debug("Loading http texture from local cache ({})", this.file);
				FileInputStream fileinputstream = new FileInputStream(this.file);
				nativeimage = this.load(fileinputstream);
			} else
			{
				nativeimage = null;
			}
			
			if(nativeimage != null)
			{
				this.loadCallback(nativeimage);
			} else
			{
				this.future = CompletableFuture.runAsync(() ->
				{
					LOGGER.debug("Downloading http texture from {} to {}", this.urlString, this.file);
					
					try(var req = HttpRequest.get(this.urlString)
							.useProxy(Minecraft.getInstance().getProxy())
							.headers(EXTRA_HEADERS))
					{
						if(req.code() / 100 == 2)
						{
							InputStream inputstream;
							if(this.file != null)
							{
								FileUtils.copyInputStreamToFile(req.stream(), this.file);
								inputstream = new FileInputStream(this.file);
							} else
							{
								inputstream = req.stream();
							}
							
							NativeImage image = this.load(inputstream);
							
							Minecraft.getInstance().execute(() ->
							{
								if(image != null) this.loadCallback(image);
							});
						}
					} catch(Exception exception)
					{
						LOGGER.error("Couldn't download http texture", exception);
					}
					
				}, Util.backgroundExecutor());
			}
		}
	}
	
	private void loadCallback(NativeImage image)
	{
		if(this.onDownloaded != null)
		{
			this.onDownloaded.run();
		}
		
		Minecraft.getInstance().execute(() ->
		{
			this.uploaded = true;
			if(!RenderSystem.isOnRenderThread())
			{
				RenderSystem.recordRenderCall(() ->
				{
					this.upload(image);
				});
			} else
			{
				this.upload(image);
			}
		});
	}
	
	private void upload(NativeImage image)
	{
		TextureUtil.prepareImage(this.getId(), image.getWidth(), image.getHeight());
		image.upload(0, 0, 0, true);
	}
	
	@javax.annotation.Nullable
	private NativeImage load(InputStream stream)
	{
		NativeImage nativeimage = null;
		
		try
		{
			nativeimage = NativeImage.read(stream);
		} catch(Exception exception)
		{
			LOGGER.warn("Error while loading the URL texture", exception);
		}
		
		return nativeimage;
	}
}