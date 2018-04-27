package com.pengu.hammercore.tile.tooltip.own.inf;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public class TranslationTooltipInfo extends StringTooltipInfo
{
	public FontRenderer fontRenderer;
	public int color = 0xFF_FF_FF_FF;
	public boolean dropShadow = true;
	protected String colors = "";
	
	public TranslationTooltipInfo(String i18n)
	{
		super(i18n);
	}
	
	@Override
	public String getText()
	{
		return colors + I18n.format(text);
	}
	
	@Override
	public StringTooltipInfo appendColor(TextFormatting tf)
	{
		colors = tf + colors;
		return this;
	}
}