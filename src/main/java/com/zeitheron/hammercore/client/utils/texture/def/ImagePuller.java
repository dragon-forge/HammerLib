package com.zeitheron.hammercore.client.utils.texture.def;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.client.utils.IImagePreprocessor;
import com.zeitheron.hammercore.client.utils.texture.TexLocUploader;
import com.zeitheron.hammercore.lib.zlib.io.IOUtils;
import com.zeitheron.hammercore.lib.zlib.utils.Threading;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * This class downloads images from http and https without blocking the caller
 * thread. Useful for rendering
 */
public class ImagePuller
{
	private static BufferedImage nosignal = null;
	public static ResourceLocation nosignal_gl = new ResourceLocation("hammercore", "textures/nosignal.png");
	
	public static final Map<String, Thread> DWN = new HashMap<>();
	public static final Map<String, ResourceLocation> GL_IMAGES = new HashMap<>();
	
	@Nullable
	public static BufferedImage noSignalBI()
	{
		if(nosignal == null)
			try
			{
				nosignal = ImageIO.read(HammerCore.class.getResourceAsStream("/assets/hammercore/textures/nosignal.png"));
			} catch(Throwable err)
			{
			}
		return nosignal;
	}
	
	public static ResourceLocation getImageGL(String url, IImagePreprocessor... proc)
	{
		if(url == null || url.isEmpty() || !url.toLowerCase().startsWith("http"))
			return nosignal_gl;
		if(DWN.get(url) != null)
			return nosignal_gl;
		else if(GL_IMAGES.get(url) == null)
			download(url, proc);
		return GL_IMAGES.getOrDefault(url, nosignal_gl);
	}
	
	public static boolean bind(String url, IImagePreprocessor... proc)
	{
		ResourceLocation gl = getImageGL(url, proc);
		if(gl != null)
			Minecraft.getMinecraft().getTextureManager().bindTexture(gl);
		return gl != null;
	}
	
	private static void download(String url, IImagePreprocessor... proc)
	{
		ResourceLocation rl = getLocationForURL(url);
		HammerCore.LOG.info("Start download " + rl);
		DWN.put(url, Threading.createAndStart(() ->
		{
			BufferedImage img = IOUtils.downloadPicture(url);
			
			if(img == null)
				img = noSignalBI();
			else
				for(IImagePreprocessor p : proc)
					img = p.process(img);
			
			final BufferedImage icon = img;
			
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				TexLocUploader.upload(rl, icon);
				GL_IMAGES.put(url, rl);
			});
			
			DWN.remove(url);
		}));
	}
	
	public static ResourceLocation getLocationForURL(String url)
	{
		return new ResourceLocation("hammercore", url.substring(url.indexOf("://") + 3));
	}
}