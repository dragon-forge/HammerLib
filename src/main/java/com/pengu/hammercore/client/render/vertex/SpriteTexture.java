package com.pengu.hammercore.client.render.vertex;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;

public abstract class SpriteTexture
{
	public abstract void bind();
	
	public abstract TextureAtlasSprite getSprite();
	
	public abstract void setSprite(TextureAtlasSprite sprite);
	
	public static class BlockSpriteTexture extends SpriteTexture
	{
		private TextureAtlasSprite sprite;
		
		@Override
		public void bind()
		{
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		}
		
		@Override
		public TextureAtlasSprite getSprite()
		{
			return sprite;
		}
		
		@Override
		public void setSprite(TextureAtlasSprite sprite)
		{
			this.sprite = sprite;
		}
	}
}