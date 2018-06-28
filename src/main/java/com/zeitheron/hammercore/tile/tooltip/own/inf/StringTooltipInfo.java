package com.zeitheron.hammercore.tile.tooltip.own.inf;

import com.zeitheron.hammercore.tile.tooltip.own.IRenderableInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextFormatting;

public class StringTooltipInfo implements IRenderableInfo
{
	protected String text;
	public FontRenderer fontRenderer;
	public int color = 0xFF_FF_FF_FF;
	public boolean dropShadow = true;
	
	public StringTooltipInfo(String text)
	{
		this.text = text;
		this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	@Override
	public int getWidth()
	{
		return fontRenderer.getStringWidth(getText());
	}
	
	@Override
	public int getHeight()
	{
		return fontRenderer.FONT_HEIGHT;
	}
	
	@Override
	public void render(float x, float y, float partialTime)
	{
		fontRenderer.drawString(getText(), x, y, color, dropShadow);
	}
	
	public StringTooltipInfo appendColor(TextFormatting tf)
	{
		text = tf.toString() + text;
		return this;
	}
}