package com.zeitheron.hammercore.utils.color;

public class ColorARGB extends Color
{
	public ColorARGB(int color)
	{
		super((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, (color >> 24) & 0xFF);
	}
	
	public ColorARGB(int a, int r, int g, int b)
	{
		super(r, g, b, a);
	}
	
	public ColorARGB(ColorARGB color)
	{
		super(color);
	}
	
	@Override
	public ColorARGB copy()
	{
		return new ColorARGB(this);
	}
	
	@Override
	public Color set(int color)
	{
		return set(new ColorARGB(color));
	}
	
	@Override
	public int pack()
	{
		return pack(this);
	}
	
	@Override
	public float[] packArray()
	{
		return new float[] { (a & 0xFF) / 255, (r & 0xFF) / 255, (g & 0xFF) / 255, (b & 0xFF) / 255 };
	}
	
	public static int pack(Color color)
	{
		return (color.a & 0xFF) << 24 | (color.r & 0xFF) << 16 | (color.g & 0xFF) << 8 | (color.b & 0xFF);
	}
}