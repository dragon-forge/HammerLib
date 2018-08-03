package com.zeitheron.hammercore.client.utils.texture;

import java.util.ArrayList;
import java.util.HashMap;

import com.zeitheron.hammercore.client.utils.texture.TextureUtils.IIconRegister;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpriteSheetManager
{
	@SideOnly(Side.CLIENT)
	public static class SpriteSheet implements IIconRegister
	{
		private int tilesX;
		private int tilesY;
		private ArrayList<Integer> newSprites = new ArrayList<>();
		private TextureSpriteCustom[] sprites;
		private ResourceLocation resource;
		private TextureDataHolder texture;
		private int spriteWidth;
		private int spriteHeight;
		
		public int atlasIndex;
		
		private SpriteSheet(int tilesX, int tilesY, ResourceLocation textureFile)
		{
			this.tilesX = tilesX;
			this.tilesY = tilesY;
			this.resource = textureFile;
			sprites = new TextureSpriteCustom[tilesX * tilesY];
		}
		
		public void requestIndicies(int... indicies)
		{
			for(int i : indicies)
			{
				setupSprite(i);
			}
		}
		
		@Override
		public void registerIcons(TextureMap textureMap)
		{
			if(TextureUtils.refreshTexture(textureMap, resource.getPath()))
			{
				reloadTexture();
				for(TextureSpriteCustom sprite : sprites)
				{
					if(sprite != null)
					{
						textureMap.setTextureEntry(sprite);
					}
				}
			} else
			{
				for(int i : newSprites)
				{
					textureMap.setTextureEntry(sprites[i]);
				}
			}
			newSprites.clear();
		}
		
		public TextureSpriteCustom setupSprite(int i)
		{
			if(sprites[i] == null)
			{
				String name = resource + "_" + i;
				sprites[i] = new TextureSpriteCustom(name).baseFromSheet(this, i);
				newSprites.add(i);
			}
			return sprites[i];
		}
		
		private void reloadTexture()
		{
			texture = TextureUtils.loadTexture(resource);
			spriteWidth = texture.width / tilesX;
			spriteHeight = texture.height / tilesY;
		}
		
		public TextureAtlasSprite getSprite(int index)
		{
			TextureAtlasSprite i = sprites[index];
			if(i == null)
			{
				throw new IllegalArgumentException("Sprite at index: " + index + " from texture file " + resource + " was not preloaded.");
			}
			return i;
		}
		
		public TextureDataHolder createSprite(int spriteIndex)
		{
			int sx = spriteIndex % tilesX;
			int sy = spriteIndex / tilesX;
			TextureDataHolder sprite = new TextureDataHolder(spriteWidth, spriteHeight);
			TextureUtils.copySubImg(texture.data, texture.width, sx * spriteWidth, sy * spriteHeight, spriteWidth, spriteHeight, sprite.data, spriteWidth, 0, 0);
			return sprite;
		}
		
		public int spriteWidth()
		{
			return spriteWidth;
		}
		
		public int spriteHeight()
		{
			return spriteHeight;
		}
		
		public TextureSpriteCustom bindTextureFX(int i, TextureSpriteFX textureFX)
		{
			return setupSprite(i).addTextureFX(textureFX);
		}
		
		public SpriteSheet selfRegister(int atlas)
		{
			TextureUtils.addIconRegister(this);
			return this;
		}
	}
	
	private static HashMap<String, SpriteSheet> spriteSheets = new HashMap<>();
	
	public static SpriteSheet getSheet(ResourceLocation resource)
	{
		return getSheet(16, 16, resource);
	}
	
	public static SpriteSheet getSheet(int tilesX, int tilesY, ResourceLocation resource)
	{
		SpriteSheet sheet = spriteSheets.get(resource.toString());
		if(sheet == null)
		{
			spriteSheets.put(resource.toString(), sheet = new SpriteSheet(tilesX, tilesY, resource));
		}
		return sheet;
	}
}