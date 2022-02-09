package org.zeith.hammerlib.tiles.tooltip.own.inf;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;

public class TranslationTooltipInfo
		extends StringTooltipInfo
{
	public Font fontRenderer;
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
		return colors + I18n.get(text);
	}

	@Override
	public StringTooltipInfo appendColor(ChatFormatting tf)
	{
		colors = tf + colors;
		return this;
	}
}