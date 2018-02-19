package com.pengu.hammercore.client.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class URLImageTexture extends AbstractTexture
{
	private static final Logger LOG = LogManager.getLogger();
	protected final ResourceLocation textureLocation;
	protected String url;
	
	public URLImageTexture(ResourceLocation textureResourceLocation, String url)
	{
		this.textureLocation = textureResourceLocation;
		this.url = url;
	}
	
	@Override
	public void loadTexture(IResourceManager resourceManager) throws IOException
	{
		this.deleteGlTexture();
		
		InputStream input = null;
		try
		{
			BufferedImage bufferedimage = TextureUtil.readBufferedImage(input = new URL(url).openStream());
			TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage, false, false);
		} finally
		{
			if(input != null)
				IOUtils.closeQuietly(input);
		}
	}
}