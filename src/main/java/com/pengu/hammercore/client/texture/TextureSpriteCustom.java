package com.pengu.hammercore.client.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

import com.pengu.hammercore.client.texture.SpriteSheetManager.SpriteSheet;
import com.pengu.hammercore.client.texture.TextureUtils.IIconRegister;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.PngSizeInfo;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class TextureSpriteCustom extends TextureAtlasSprite implements IIconRegister
{
	// sprite sheet fields
	private int spriteIndex;
	private SpriteSheet spriteSheet;
	
	// textureFX fields
	private TextureSpriteFX textureFX;
	private int mipmapLevels;
	private int rawWidth;
	private int rawHeight;
	
	private int blankSize = -1;
	private ArrayList<TextureDataHolder> baseTextures;
	
	private boolean selfRegister;
	public int atlasIndex;
	
	public static TextureSpriteCustom createSprite(ResourceLocation path)
	{
		TextureSpriteCustom sprite = SpriteSheetManager.getSheet(path).setupSprite(0);
		TextureFXManager.INSTANCE.registerIcon(path.toString(), sprite);
		return sprite;
	}
	
	public void resetFX(int pass)
	{
		if(textureFX != null)
			textureFX.reload(pass);
	}
	
	protected TextureSpriteCustom(String par1)
	{
		super(par1);
	}
	
	public TextureSpriteCustom addTexture(TextureDataHolder t)
	{
		if(baseTextures == null)
			baseTextures = new ArrayList<>();
		baseTextures.add(t);
		return this;
	}
	
	public TextureSpriteCustom baseFromSheet(SpriteSheet spriteSheet, int spriteIndex)
	{
		this.spriteSheet = spriteSheet;
		this.spriteIndex = spriteIndex;
		return this;
	}
	
	public TextureSpriteCustom addTextureFX(TextureSpriteFX fx)
	{
		textureFX = fx;
		return this;
	}
	
	@Override
	public void initSprite(int sheetWidth, int sheetHeight, int originX, int originY, boolean rotated)
	{
		super.initSprite(sheetWidth, sheetHeight, originX, originY, rotated);
		if(textureFX != null)
		{
			textureFX.onTextureDimensionsUpdate(width, height);
		}
	}
	
	@Override
	public void updateAnimation()
	{
		if(textureFX != null)
		{
			textureFX.update();
			if(textureFX.changed())
			{
				int[][] mipmaps = new int[mipmapLevels + 1][];
				mipmaps[0] = textureFX.imageData;
				mipmaps = TextureUtil.generateMipmapData(mipmapLevels, width, mipmaps);
				TextureUtil.uploadTextureMipmap(mipmaps, width, height, originX, originY, false, false);
			}
		}
	}
	
	@Override
	public void loadSprite(PngSizeInfo sizeInfo, boolean animationMeta)
	{
		rawWidth = sizeInfo.pngWidth;
		rawHeight = sizeInfo.pngHeight;
		try
		{
			super.loadSprite(sizeInfo, false);
		} catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void generateMipmaps(int p_147963_1_)
	{
		super.generateMipmaps(p_147963_1_);
		mipmapLevels = p_147963_1_;
	}
	
	@Override
	public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location)
	{
		return true;
	}
	
	public void addFrame(int[] data, int width, int height)
	{
		GameSettings settings = Minecraft.getMinecraft().gameSettings;
		BufferedImage[] images = new BufferedImage[settings.mipmapLevels + 1];
		images[0] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		images[0].setRGB(0, 0, width, height, data, 0, width);
		
		// Workaround for mojang's change to TextureAtlasSprite loading.
		resetSprite();
		this.width = images[0].getWidth();
		this.height = images[0].getHeight();
		int[][] aInt = new int[settings.mipmapLevels + 1][];
		aInt[0] = new int[images[0].getWidth() * images[0].getHeight()];
		images[0].getRGB(0, 0, images[0].getWidth(), images[0].getHeight(), aInt[0], 0, images[0].getWidth());
		framesTextureData.add(aInt);
	}
	
	@Override
	public boolean load(IResourceManager manager, ResourceLocation location, Function<ResourceLocation, TextureAtlasSprite> textureGetter)
	{
		if(baseTextures != null)
		{
			for(TextureDataHolder tex : baseTextures)
				addFrame(tex.data, tex.width, tex.height);
		} else if(spriteSheet != null)
		{
			TextureDataHolder tex = spriteSheet.createSprite(spriteIndex);
			addFrame(tex.data, tex.width, tex.height);
		} else if(blankSize > 0)
		{
			addFrame(new int[blankSize * blankSize], blankSize, blankSize);
		}
		
		if(framesTextureData.isEmpty())
			throw new RuntimeException("No base frame for texture: " + getIconName());
		return false;
	}
	
	@Override
	public boolean hasAnimationMetadata()
	{
		return textureFX != null || super.hasAnimationMetadata();
	}
	
	@Override
	public int getFrameCount()
	{
		if(textureFX != null)
			return 1;
		return super.getFrameCount();
	}
	
	public TextureSpriteCustom blank(int size)
	{
		blankSize = size;
		return this;
	}
	
	public TextureSpriteCustom selfRegister()
	{
		selfRegister = true;
		TextureUtils.addIconRegister(this);
		return this;
	}
	
	@Override
	public void registerIcons(TextureMap textureMap)
	{
		textureMap.setTextureEntry(this);
	}
}