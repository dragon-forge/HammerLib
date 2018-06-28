package com.zeitheron.hammercore.client.utils;

public interface IPixelGetter
{
	int getWidth();
	
	int getHeight();
	
	int getPixel(int x, int y);
}