package com.pengu.hammercore.utils;

public class ColorHelper
{
	public static String getColorName(int rgb)
	{
		return ColorNamePicker.getColorNameFromHex(rgb);
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
}