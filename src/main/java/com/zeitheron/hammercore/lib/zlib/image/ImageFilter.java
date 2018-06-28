package com.zeitheron.hammercore.lib.zlib.image;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.Map;

public interface ImageFilter
{
	ImageFilter BLACK_AND_WHITE = (img, settings) ->
	{
		for(int x = 0; x < img.getWidth(); ++x)
			for(int y = 0; y < img.getHeight(); ++y)
			{
				int rgb = img.getRGB(x, y);
				float r = (rgb >> 16 & 0xFF) / 255F, g = (rgb >> 8 & 0xFF) / 255F, b = (rgb & 0xFF) / 255F;
				int bri = (int) (r * g * b * 255F);
				rgb = 255 << 24 | bri << 16 | bri << 8 | bri;
				img.setRGB(x, y, rgb);
			}
		
		return img;
	};
	
	ImageFilter BRIGHTEN = (img, settings) ->
	{
		RescaleOp rescaleOp = new RescaleOp(1.2F, settings.hasSetting("bright", EnumFilterSettingType.INT) ? settings.getInt("bright") : 15, null);
		rescaleOp.filter(img, img);
		return img;
	};
	
	default BufferedImage applyToCopy(BufferedImage img, iFilterSettings settings)
	{
		BufferedImage i = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		i.createGraphics().drawImage(img, 0, 0, null);
		return apply(i, settings);
	}
	
	BufferedImage apply(BufferedImage bi, iFilterSettings settings);
	
	public static interface iFilterSettings
	{
		iFilterSettings NONE = id -> null;
		
		public static iFilterSettings of(Map<String, Object> map)
		{
			return id -> map.get(id);
		}
		
		default boolean hasSetting(String id)
		{
			return hasSetting(id, EnumFilterSettingType.ANY);
		}
		
		default boolean hasSetting(String id, EnumFilterSettingType type)
		{
			if(get(id) == null)
				return false;
			if(type == EnumFilterSettingType.ANY)
				return true;
			if(type == EnumFilterSettingType.INT && get(id) instanceof Integer)
				return true;
			if(type == EnumFilterSettingType.BOOL && get(id) instanceof Boolean)
				return true;
			if(type == EnumFilterSettingType.FLOAT && get(id) instanceof Float)
				return true;
			return false;
		}
		
		default int getInt(String id)
		{
			Object o = get(id);
			if(o instanceof Number)
				return ((Number) o).intValue();
			return 0;
		}
		
		default boolean getBool(String id)
		{
			Object o = get(id);
			if(o instanceof Boolean)
				return ((Boolean) o).booleanValue();
			return false;
		}
		
		default float getFloat(String id)
		{
			Object o = get(id);
			if(o instanceof Number)
				return ((Number) o).floatValue();
			return 0;
		}
		
		Object get(String id);
	}
	
	public static enum EnumFilterSettingType
	{
		INT, BOOL, FLOAT, ANY;
	}
}