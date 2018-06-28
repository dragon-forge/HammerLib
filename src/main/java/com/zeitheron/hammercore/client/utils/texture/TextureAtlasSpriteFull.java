package com.zeitheron.hammercore.client.utils.texture;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class TextureAtlasSpriteFull extends TextureAtlasSprite
{
	public static final TextureAtlasSpriteFull sprite = new TextureAtlasSpriteFull("full");
	
	protected TextureAtlasSpriteFull(String spriteName)
	{
		super(spriteName);
		width = 256;
		height = 256;
		initSprite(256, 256, 0, 0, false);
	}
	
	@Override
	public float getMinU()
	{
		width = 256;
		height = 256;
		initSprite(256, 256, 0, 0, false);
		
		return super.getMinU();
	}
	
	@Override
	public float getMinV()
	{
		return super.getMinV();
	}
	
	@Override
	public float getMaxU()
	{
		return super.getMaxU();
	}
	
	@Override
	public float getMaxV()
	{
		return super.getMaxV();
	}
}