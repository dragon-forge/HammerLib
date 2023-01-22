package org.zeith.hammerlib.tiles.tooltip.own.inf;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.tiles.tooltip.own.IRenderableInfo;

public class TooltipInfoText
		implements IRenderableInfo
{
	protected MutableComponent text;
	public Font fontRenderer;
	public boolean dropShadow = true;
	
	public TooltipInfoText(Component text)
	{
		this.text = text instanceof MutableComponent mc ? mc : text.copy();
		this.fontRenderer = Minecraft.getInstance().font;
	}
	
	public void setDropShadow(boolean dropShadow)
	{
		this.dropShadow = dropShadow;
	}
	
	public void setText(MutableComponent text)
	{
		this.text = text;
	}
	
	@Override
	public float getWidth()
	{
		return fontRenderer.width(getText());
	}
	
	@Override
	public float getHeight()
	{
		return fontRenderer.lineHeight;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(PoseStack matrix, float x, float y, float partialTime)
	{
		if(dropShadow) fontRenderer.drawShadow(matrix, getText(), x, y, 0xFFFFFFFF);
		else fontRenderer.draw(matrix, getText(), x, y, 0xFFFFFFFF);
	}
	
	public MutableComponent getText()
	{
		return text;
	}
}