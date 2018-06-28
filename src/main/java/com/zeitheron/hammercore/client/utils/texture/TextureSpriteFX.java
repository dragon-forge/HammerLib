package com.zeitheron.hammercore.client.utils.texture;

import com.zeitheron.hammercore.client.utils.texture.SpriteSheetManager.SpriteSheet;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TextureSpriteFX
{
	public int[] imageData;
	public int tileSizeBase = 16;
	public int tileSizeSquare = 256;
	public int tileSizeMask = 15;
	public int tileSizeSquareMask = 255;
	
	public boolean anaglyphEnabled;
	public TextureSpriteCustom texture;
	
	public TextureSpriteFX(int spriteIndex, SpriteSheet sheet)
	{
		texture = sheet.bindTextureFX(spriteIndex, this);
	}
	
	public TextureSpriteFX(int size, String name)
	{
		texture = new TextureSpriteCustom(name).blank(size).selfRegister().addTextureFX(this);
	}
	
	public TextureSpriteFX setAtlas(int index)
	{
		texture.atlasIndex = index;
		return this;
	}
	
	public void setup()
	{
		imageData = new int[tileSizeSquare];
	}
	
	public void onTextureDimensionsUpdate(int width, int height)
	{
		if(width != height)
			throw new IllegalArgumentException("Non-Square textureFX not supported (" + width + ":" + height + ")");
		tileSizeBase = width;
		tileSizeSquare = tileSizeBase * tileSizeBase;
		tileSizeMask = tileSizeBase - 1;
		tileSizeSquareMask = tileSizeSquare - 1;
		setup();
	}
	
	public void update()
	{
		anaglyphEnabled = Minecraft.getMinecraft().gameSettings.anaglyph;
		onTick();
	}
	
	public void reload(int pass)
	{
		
	}
	
	public void onTick()
	{
	}
	
	public boolean changed()
	{
		return true;
	}
}