package com.zeitheron.hammercore.tile.tooltip.own.inf;

import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.hammercore.client.utils.texture.TextureAtlasSpriteFull;
import com.zeitheron.hammercore.tile.tooltip.own.IRenderableInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class TextureTooltipInfo implements IRenderableInfo
{
	public ResourceLocation texture;
	public int width, height;
	
	public TextureTooltipInfo(ResourceLocation texture, int width, int height)
	{
		this.texture = texture;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public int getWidth()
	{
		return this.width;
	}
	
	@Override
	public int getHeight()
	{
		return this.height;
	}
	
	@Override
	public void render(float x, float y, float partialTime)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		RenderUtil.drawTexturedModalRect(x, y, TextureAtlasSpriteFull.sprite, width, height);
	}
}