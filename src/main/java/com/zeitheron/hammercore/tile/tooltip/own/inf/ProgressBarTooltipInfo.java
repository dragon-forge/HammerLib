package com.zeitheron.hammercore.tile.tooltip.own.inf;

import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.hammercore.client.utils.texture.TextureAtlasSpriteFull;
import com.zeitheron.hammercore.tile.tooltip.ProgressBar;
import com.zeitheron.hammercore.tile.tooltip.own.IRenderableInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class ProgressBarTooltipInfo implements IRenderableInfo
{
	public ProgressBar bar;
	
	public ProgressBarTooltipInfo(ProgressBar bar)
	{
		this.bar = bar;
	}
	
	@Override
	public int getWidth()
	{
		return 101;
	}
	
	@Override
	public int getHeight()
	{
		return 12;
	}
	
	@Override
	public void render(float x, float y, float partialTime)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		Gui.drawRect(0, 0, 101, 12, bar.borderColor);
		Gui.drawRect(1, 1, 100, 11, bar.backgroundColor);
		
		int fill = 1 + bar.getProgressPercent();
		
		Gui.drawRect(1, 1, fill, 11, bar.filledMainColor);
		
		for(int j = 0; j < fill; j += 2)
			Gui.drawRect(1 + j, 1, 2 + j, 11, bar.filledAlternateColor);
		
		Minecraft.getMinecraft().fontRenderer.drawString((bar.prefix != null ? bar.prefix : "") + (bar.suffix != null ? bar.suffix : ""), 3, 2, 0xFFFFFF, true);
		GL11.glPopMatrix();
	}
}