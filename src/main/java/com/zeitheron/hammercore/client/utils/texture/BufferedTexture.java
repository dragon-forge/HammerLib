package com.zeitheron.hammercore.client.utils.texture;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BufferedTexture extends AbstractTexture
{
	private static final Logger LOG = LogManager.getLogger();
	protected final BufferedImage texture;
	protected final boolean blur, clamp;
	
	public BufferedTexture(BufferedImage img, boolean blur, boolean clamp)
	{
		this.texture = img;
		this.blur = blur;
		this.clamp = clamp;
	}
	
	public BufferedTexture(BufferedImage img)
	{
		this.texture = img;
		this.blur = false;
		this.clamp = false;
	}
	
	@Override
	public void loadTexture(IResourceManager resourceManager) throws IOException
	{
		this.deleteGlTexture();
		TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), this.texture, blur, clamp);
	}
}