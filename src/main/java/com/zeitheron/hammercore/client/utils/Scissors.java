package com.zeitheron.hammercore.client.utils;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * Adapted GL scissor for minecraft pixel resolution and adjusts (0;0) to
 * left-top corner.
 */
public class Scissors
{
	public static void begin()
	{
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
	}
	
	public static void scissor(int x, int y, int width, int height)
	{
		int sw = Minecraft.getMinecraft().displayWidth;
		int sh = Minecraft.getMinecraft().displayHeight;
		
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		
		float dw = sr.getScaledWidth();
		float dh = sr.getScaledHeight();
		
		x = Math.round(sw * (x / dw));
		y = Math.round(sh * (y / dh));
		
		width = Math.round(sw * (width / dw));
		height = Math.round(sh * (height / dh));
		
		GL11.glScissor(x, sh - height - y, width, height);
	}
	
	public static void end()
	{
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
}