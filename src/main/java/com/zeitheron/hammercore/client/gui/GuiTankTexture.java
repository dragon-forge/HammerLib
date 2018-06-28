package com.zeitheron.hammercore.client.gui;

import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.client.utils.RenderUtil;

import net.minecraft.client.Minecraft;

public class GuiTankTexture
{
	public final int width, height;
	public final int tankColor;
	
	public GuiTankTexture(int width, int height)
	{
		this(width, height, 0xFF7F0000);
	}
	
	public GuiTankTexture(int width, int height, int tankColor)
	{
		this.tankColor = tankColor;
		this.width = width;
		this.height = height;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void draw(int xOffset, int yOffset)
	{
		int totalLines = height / 5;
		int half = totalLines / 2;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		for(int l = 1; l < totalLines; ++l)
			RenderUtil.drawColoredModalRect(xOffset, yOffset + l * 5, width / (l == half ? 1 : 2), 1, tankColor);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}