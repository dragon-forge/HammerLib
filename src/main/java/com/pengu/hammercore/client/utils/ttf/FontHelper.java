package com.pengu.hammercore.client.utils.ttf;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class FontHelper
{
	private static String formatEscape = "\u00a7";
	
	public static void drawString(String s, float x, float y, TrueTypeFont font, float scaleX, float scaleY, int format, float... rgba)
	{
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution sr = new ScaledResolution(mc);
		int amt = 1;
		if(sr.getScaleFactor() == 1)
			amt = 2;
		FloatBuffer matrixData = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(2982, matrixData);
		Matrix4f matrix = new Matrix4f();
		matrix.load(matrixData);
		FontHelper.set2DMode();
		y = mc.displayHeight - y * sr.getScaleFactor() - font.getLineHeight() / amt;
		GL11.glEnable(3042);
		if(s.contains(formatEscape))
		{
			String[] pars = s.split(formatEscape);
			float totalOffset = 0.0f;
			for(int i = 0; i < pars.length; ++i)
			{
				String par = pars[i];
				float[] c = rgba;
				if(i > 0)
				{
					c = Formatter.getFormatted(par.charAt(0));
					par = par.substring(1, par.length());
				}
				font.drawString(x * sr.getScaleFactor() + totalOffset, y - matrix.m31 * sr.getScaleFactor(), par, scaleX / amt, scaleY / amt, format, c);
				totalOffset += font.getWidth(par);
			}
		} else
			font.drawString(x * sr.getScaleFactor(), y - matrix.m31 * sr.getScaleFactor(), s, scaleX / amt, scaleY / amt, format, rgba);
		GL11.glDisable(3042);
		FontHelper.set3DMode();
	}
	
	private static void set2DMode()
	{
		Minecraft mc = Minecraft.getMinecraft();
		GL11.glMatrixMode(5889);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glOrtho(0, mc.displayWidth, 0, mc.displayHeight, -1, 1);
		GL11.glMatrixMode(5888);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
	}
	
	private static void set3DMode()
	{
		GL11.glMatrixMode(5889);
		GL11.glPopMatrix();
		GL11.glMatrixMode(5888);
		GL11.glPopMatrix();
	}
}