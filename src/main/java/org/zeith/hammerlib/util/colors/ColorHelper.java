package org.zeith.hammerlib.util.colors;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.util.java.NumberUtils;

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
	
	@OnlyIn(Dist.CLIENT)
	public static void glColor1ia(int argb)
	{
		RenderSystem.setShaderColor(getRed(argb), getGreen(argb), getBlue(argb), getAlpha(argb));
	}
	
	@OnlyIn(Dist.CLIENT)
	public static void glColor1i(int argb)
	{
		RenderSystem.setShaderColor(getRed(argb), getGreen(argb), getBlue(argb), 1F);
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
	
	public static int packARGBi(int a, int r, int g, int b)
	{
		return (a << 24) | (r << 16) | (b << 8) | b;
	}
	
	public static int packRGBi(int r, int g, int b)
	{
		return (r << 16) | (b << 8) | b;
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
	
	public static int getAlphai(int rgb)
	{
		return (rgb >> 24) & 0xFF;
	}
	
	public static int getRedi(int rgb)
	{
		return (rgb >> 16) & 0xFF;
	}
	
	public static int getGreeni(int rgb)
	{
		return (rgb >> 8) & 0xFF;
	}
	
	public static int getBluei(int rgb)
	{
		return (rgb >> 0) & 0xFF;
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
	
	public static float luma(float red, float green, float blue)
	{
		return red * 0.299F + green * 0.587F + blue * 0.114F;
	}
	
	public static float lumai(int red, int green, int blue)
	{
		return red / 255F * 0.299F + green / 255F * 0.587F + blue / 255F * 0.114F;
	}
	
	public static int interpolateSine(int a, int b, float progress)
	{
		return interpolate(a, b, progress <= 0F ? 0F : progress >= 1F ? 1F : NumberUtils.progressToSinef(progress));
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