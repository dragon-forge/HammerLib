package com.pengu.hammercore.client.texture;

import java.io.IOException;
import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.PngSizeInfo;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class TextureAtlasSpriteResource extends TextureAtlasSprite
{
	private final ResourceLocation location;
	
	protected TextureAtlasSpriteResource(ResourceLocation location)
	{
		super(location.toString());
		this.location = location;
	}
	
	@Override
	public void loadSprite(PngSizeInfo sizeInfo, boolean p_188538_2_) throws IOException
	{
	}
	
	@Override
	public void loadSpriteFrames(IResource resource, int mipmaplevels) throws IOException
	{
	}
	
	@Override
	public boolean load(IResourceManager manager, ResourceLocation location, Function<ResourceLocation, TextureAtlasSprite> textureGetter)
	{
		return false;
	}
	
	public void bind()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(location);
	}
	
	/**
	 * Returns the minimum U coordinate to use when rendering with this icon.
	 */
	public float getMinU()
	{
		return 0;
	}
	
	/**
	 * Returns the maximum U coordinate to use when rendering with this icon.
	 */
	public float getMaxU()
	{
		return 1;
	}
	
	/**
	 * Returns the minimum V coordinate to use when rendering with this icon.
	 */
	public float getMinV()
	{
		return 0;
	}
	
	/**
	 * Returns the maximum V coordinate to use when rendering with this icon.
	 */
	public float getMaxV()
	{
		return 1;
	}
	
	/**
	 * Gets a U coordinate on the icon. 0 returns uMin and 16 returns uMax.
	 * Other arguments return in-between values.
	 */
	public float getInterpolatedU(double u)
	{
		float f = getMaxU() - getMinU();
		return getMinU() + f * (float) u / 16.0F;
	}
	
	/**
	 * The opposite of getInterpolatedU. Takes the return value of that method
	 * and returns the input to it.
	 */
	public float getUnInterpolatedU(float u)
	{
		float f = getMaxU() - getMinU();
		return (u - getMinU()) / f * 16.0F;
	}
	
	/**
	 * Gets a V coordinate on the icon. 0 returns vMin and 16 returns vMax.
	 * Other arguments return in-between values.
	 */
	public float getInterpolatedV(double v)
	{
		float f = getMaxV() - getMinV();
		return getMinV() + f * (float) v / 16.0F;
	}
	
	/**
	 * The opposite of getInterpolatedV. Takes the return value of that method
	 * and returns the input to it.
	 */
	public float getUnInterpolatedV(float p_188536_1_)
	{
		float f = getMaxV() - getMinV();
		return (p_188536_1_ - getMinV()) / f * 16.0F;
	}
}