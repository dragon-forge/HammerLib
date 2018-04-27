package com.pengu.hammercore.tile.tooltip.own;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IRenderableInfo
{
	int getWidth();
	
	int getHeight();
	
	void render(float x, float y, float partialTime);
}