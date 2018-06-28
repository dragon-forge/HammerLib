package com.zeitheron.hammercore.utils.color;

import com.zeitheron.hammercore.utils.ICopyable;
import com.zeitheron.hammercore.utils.math.MathHelper;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Color implements ICopyable<Color>
{
	public byte r;
	public byte g;
	public byte b;
	public byte a;
	
	public Color(int r, int g, int b, int a)
	{
		this.r = (byte) r;
		this.g = (byte) g;
		this.b = (byte) b;
		this.a = (byte) a;
	}
	
	public Color(Color color)
	{
		r = color.r;
		g = color.g;
		b = color.b;
		a = color.a;
	}
	
	@SideOnly(Side.CLIENT)
	public void glColor()
	{
		GlStateManager.color((r & 0xFF) / 255F, (g & 0xFF) / 255F, (b & 0xFF) / 255F, (a & 0xFF) / 255F);
	}
	
	@SideOnly(Side.CLIENT)
	public void glColor(int a)
	{
		GlStateManager.color((r & 0xFF) / 255F, (g & 0xFF) / 255F, (b & 0xFF) / 255F, a / 255F);
	}
	
	public abstract int pack();
	
	public abstract float[] packArray();
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[0x" + Integer.toHexString(pack()).toUpperCase() + "]";
	}
	
	public Color add(Color color2)
	{
		a += color2.a;
		r += color2.r;
		g += color2.g;
		b += color2.b;
		return this;
	}
	
	public Color sub(Color color2)
	{
		int ia = (a & 0xFF) - (color2.a & 0xFF);
		int ir = (r & 0xFF) - (color2.r & 0xFF);
		int ig = (g & 0xFF) - (color2.g & 0xFF);
		int ib = (b & 0xFF) - (color2.b & 0xFF);
		a = (byte) (ia < 0 ? 0 : ia);
		r = (byte) (ir < 0 ? 0 : ir);
		g = (byte) (ig < 0 ? 0 : ig);
		b = (byte) (ib < 0 ? 0 : ib);
		return this;
	}
	
	public Color invert()
	{
		a = (byte) (0xFF - (a & 0xFF));
		r = (byte) (0xFF - (r & 0xFF));
		g = (byte) (0xFF - (g & 0xFF));
		b = (byte) (0xFF - (b & 0xFF));
		return this;
	}
	
	public Color multiply(Color color2)
	{
		a = (byte) ((a & 0xFF) * ((color2.a & 0xFF) / 255D));
		r = (byte) ((r & 0xFF) * ((color2.r & 0xFF) / 255D));
		g = (byte) ((g & 0xFF) * ((color2.g & 0xFF) / 255D));
		b = (byte) ((b & 0xFF) * ((color2.b & 0xFF) / 255D));
		return this;
	}
	
	public Color scale(double d)
	{
		a = (byte) ((a & 0xFF) * d);
		r = (byte) ((r & 0xFF) * d);
		g = (byte) ((g & 0xFF) * d);
		b = (byte) ((b & 0xFF) * d);
		return this;
	}
	
	public Color interpolate(Color color2, double d)
	{
		return this.add(color2.copy().sub(this).scale(d));
	}
	
	public static int interpolateARGB(int ca, int cb, double d)
	{
		int a1 = (ca >> 24) & 0xFF;
		int a2 = (cb >> 24) & 0xFF;
		int r1 = (ca >> 16) & 0xFF;
		int r2 = (cb >> 16) & 0xFF;
		int g1 = (ca >> 8) & 0xFF;
		int g2 = (cb >> 8) & 0xFF;
		int b1 = (ca >> 0) & 0xFF;
		int b2 = (cb >> 0) & 0xFF;
		int inta = (int) ((a1 - a2) * d) + a1;
		int intr = (int) ((r1 - r2) * d) + r1;
		int intg = (int) ((g1 - g2) * d) + g1;
		int intb = (int) ((b1 - b2) * d) + b1;
		return packARGB(intr, intg, intb, inta);
	}
	
	public static int interpolateRGBA(int ca, int cb, double d)
	{
		int r1 = (ca >> 24) & 0xFF;
		int r2 = (cb >> 24) & 0xFF;
		int g1 = (ca >> 16) & 0xFF;
		int g2 = (cb >> 16) & 0xFF;
		int b1 = (ca >> 8) & 0xFF;
		int b2 = (cb >> 8) & 0xFF;
		int a1 = (ca >> 0) & 0xFF;
		int a2 = (cb >> 0) & 0xFF;
		int inta = (int) ((a1 - a2) * d) + a1;
		int intr = (int) ((r1 - r2) * d) + r1;
		int intg = (int) ((g1 - g2) * d) + g1;
		int intb = (int) ((b1 - b2) * d) + b1;
		return packARGB(intr, intg, intb, inta);
	}
	
	public Color multiplyC(double d)
	{
		r = (byte) MathHelper.clip((r & 0xFF) * d, 0, 255);
		g = (byte) MathHelper.clip((g & 0xFF) * d, 0, 255);
		b = (byte) MathHelper.clip((b & 0xFF) * d, 0, 255);
		return this;
	}
	
	@Override
	public abstract Color copy();
	
	public int rgb()
	{
		return (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
	}
	
	public int argb()
	{
		return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
	}
	
	public int rgba()
	{
		return (r & 0xFF) << 24 | (g & 0xFF) << 16 | (b & 0xFF) << 8 | (a & 0xFF);
	}
	
	public abstract Color set(int color);
	
	public Color set(Color color)
	{
		r = color.r;
		g = color.g;
		b = color.b;
		a = color.a;
		return this;
	}
	
	public Color set(double r, double g, double b, double a)
	{
		return set((int) (255 * r), (int) (255 * g), (int) (255 * b), (int) (255 * a));
	}
	
	public Color set(int r, int g, int b, int a)
	{
		this.r = (byte) r;
		this.g = (byte) g;
		this.b = (byte) b;
		this.a = (byte) a;
		return this;
	}
	
	public static int packRGBA(byte r, byte g, byte b, byte a)
	{
		return (r & 0xFF) << 24 | (g & 0xFF) << 16 | (b & 0xFF) << 8 | (a & 0xFF);
	}
	
	public static int packARGB(byte r, byte g, byte b, byte a)
	{
		return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
	}
	
	public static int packRGBA(int r, int g, int b, int a)
	{
		return r << 24 | g << 16 | b << 8 | a;
	}
	
	public static int packARGB(int r, int g, int b, int a)
	{
		return a << 24 | r << 16 | g << 8 | b;
	}
	
	public static int packRGBA(double r, double g, double b, double a)
	{
		return (int) (r * 255) << 24 | (int) (g * 255) << 16 | (int) (b * 255) << 8 | (int) (a * 255);
	}
	
	public static int packARGB(double r, double g, double b, double a)
	{
		return (int) (a * 255) << 24 | (int) (r * 255) << 16 | (int) (g * 255) << 8 | (int) (b * 255);
	}
	
	public static int packRGBA(float[] data)
	{
		return packRGBA(data[0], data[1], data[2], data[3]);
	}
	
	public static int packARGB(float[] data)
	{
		return packARGB(data[0], data[1], data[2], data[3]);
	}
	
	public static void glColourRGBA(int color)
	{
		float r = ((color >> 24) & 0xFF) / 255F;
		float g = ((color >> 16) & 0xFF) / 255F;
		float b = ((color >> 8) & 0xFF) / 255F;
		float a = (color & 0xFF) / 255F;
		GlStateManager.color(r, g, b, a);
	}
	
	public static void glColourRGB(int color)
	{
		float r = ((color >> 16) & 0xFF) / 255F;
		float g = ((color >> 8) & 0xFF) / 255F;
		float b = ((color >> 0) & 0xFF) / 255F;
		GlStateManager.color(r, g, b, 1);
	}
	
	public static void glColourARGB(int color)
	{
		float r = ((color >> 16) & 0xFF) / 255F;
		float g = ((color >> 8) & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;
		float a = ((color >> 24 & 0xFF)) / 255F;
		GlStateManager.color(r, g, b, a);
	}
	
	public boolean equals(Color color)
	{
		return color != null && rgba() == color.rgba();
	}
}