package com.zeitheron.hammercore.utils.color;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ColorHelper
{
	public static String getColorName(int rgb)
	{
		return ColorNamePicker.getColorNameFromHex(rgb);
	}
	
	public static int getColorByName(String name)
	{
		Integer i = ColorNamePicker.trySearchColorFor(name);
		return i != null ? i.intValue() : 0xFFFFFF;
	}
	
	public static void glColor1ia(int argb)
	{
		GlStateManager.color(getRed(argb), getGreen(argb), getBlue(argb), getAlpha(argb));
	}
	
	public static void glColor1i(int argb)
	{
		GlStateManager.color(getRed(argb), getGreen(argb), getBlue(argb));
	}
	
	public static int multiply(int argb, float multi)
	{
		return packARGB(getAlpha(argb) * multi, getRed(argb) * multi, getGreen(argb) * multi, getBlue(argb) * multi);
	}
	
	public static int packARGB(float a, float r, float g, float b)
	{
		return (((int) (a * 255F)) << 24) | (((int) (r * 255F)) << 16) | (((int) (g * 255F)) << 8) | ((int) (b * 255F));
	}
	
	public static int packRGB(float r, float g, float b)
	{
		return (((int) (r * 255F)) << 16) | (((int) (g * 255F)) << 8) | ((int) (b * 255F));
	}
	
	public static float getAlpha(int rgb)
	{
		return ((rgb >> 24) & 0xFF) / 255F;
	}
	
	public static float getRed(int rgb)
	{
		return ((rgb >> 16) & 0xFF) / 255F;
	}
	
	public static float getGreen(int rgb)
	{
		return ((rgb >> 8) & 0xFF) / 255F;
	}
	
	public static float getBlue(int rgb)
	{
		return ((rgb >> 0) & 0xFF) / 255F;
	}
	
	public static float getBrightnessF(int rgb)
	{
		return getRed(rgb) * getGreen(rgb) * getBlue(rgb);
	}
	
	public static int getBrightnessRGB(int rgb)
	{
		int bri = (int) (getBrightnessF(rgb) * 255F);
		return bri << 16 | bri << 8 | bri;
	}
	
	@SideOnly(Side.CLIENT)
	public static void gl(int rgba)
	{
		GlStateManager.color(getRed(rgba), getGreen(rgba), getBlue(rgba), getAlpha(rgba));
	}
	
	public static int interpolateSine(int a, int b, float progress)
	{
		return interpolate(a, b, progress <= 0F ? 0F : progress >= 1F ? 1F : (float) Math.sin(Math.toRadians(progress * 90)));
	}
	
	public static int interpolate(int a, int b, float progress)
	{
		float rs = getRed(a) * (1 - progress) + getRed(b) * progress;
		float gs = getGreen(a) * (1 - progress) + getGreen(b) * progress;
		float bs = getBlue(a) * (1 - progress) + getBlue(b) * progress;
		float as = getAlpha(a) * (1 - progress) + getAlpha(b) * progress;
		return packARGB(as, rs, gs, bs);
	}
}