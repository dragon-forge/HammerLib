package org.zeith.hammerlib.tiles.tooltip.own.inf;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.tiles.tooltip.own.IRenderableInfo;

public class StringTooltipInfo
		implements IRenderableInfo
{
	protected String text;
	public FontRenderer fontRenderer;
	public int color = 0xFF_FF_FF_FF;
	public boolean dropShadow = true;

	public StringTooltipInfo(String text)
	{
		this.text = text;
		this.fontRenderer = Minecraft.getInstance().font;
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
		return fontRenderer.width(getText());
	}

	@Override
	public int getHeight()
	{
		return fontRenderer.lineHeight;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(MatrixStack matrix, float x, float y, float partialTime)
	{
		if(dropShadow) fontRenderer.drawShadow(matrix, getText(), x, y, color);
		else fontRenderer.draw(matrix, getText(), x, y, color);
	}

	public StringTooltipInfo appendColor(TextFormatting tf)
	{
		text = tf.toString() + text;
		return this;
	}
}