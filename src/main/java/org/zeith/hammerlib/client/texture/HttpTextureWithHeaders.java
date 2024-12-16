package org.zeith.hammerlib.client.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.net.HttpRequest;
import org.zeith.hammerlib.util.mcf.ModHelper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Slf4j
public class HttpTextureWithHeaders
{
	public static final Map<String, String> EXTRA_HEADERS = new HashMap<>();
	
	static
	{
		EXTRA_HEADERS.put(HttpRequest.HEADER_USER_AGENT, "HammerLib-based HTTP Texture Downloader (" + ModHelper.getModVersion(HLConstants.MOD_ID) + ")");
	}
	
	public static CompletableFuture<?> readImage(@Nullable File file, String url, @NotNull Consumer<NativeImage> loadCallback)
	{
		try
		{
			NativeImage nativeimage;
			if(file != null && file.isFile())
			{
				log.debug("Loading http texture from local cache ({})", file);
				@Cleanup FileInputStream fileinputstream = new FileInputStream(file);
				nativeimage = load(fileinputstream);
			} else
			{
				nativeimage = null;
			}
			if(nativeimage != null)
				loadCallback.accept(nativeimage);
		} catch(Exception e)
		{
			log.error("Failed to load http texture from local cache ({})", file, e);
		}
		
		return CompletableFuture.runAsync(() ->
				{
					log.debug("Downloading http texture from {} to {}", url, file);
					
					try(var req = HttpRequest.get(url)
							.useProxy(Minecraft.getInstance().getProxy())
							.headers(EXTRA_HEADERS))
					{
						if(req.code() / 100 == 2)
						{
							InputStream inputstream;
							if(file != null)
							{
								FileUtils.copyInputStreamToFile(req.stream(), file);
								inputstream = new FileInputStream(file);
							} else
							{
								inputstream = req.stream();
							}
							
							NativeImage image = load(inputstream);
							
							Minecraft.getInstance().execute(() ->
							{
								if(image != null)
									loadCallback.accept(image);
							});
						}
					} catch(Exception exception)
					{
						log.error("Couldn't download http texture", exception);
					}
					
				}, Util.backgroundExecutor()
		);
	}
	
	@Nullable
	private static NativeImage load(InputStream stream)
	{
		try
		{
			return NativeImage.read(stream);
		} catch(Exception exception)
		{
			log.warn("Error while loading the URL texture", exception);
			return null;
		}
	}
}