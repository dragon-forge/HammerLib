package org.zeith.hammerlib.tiles.tooltip.own.inf;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.network.chat.*;
import net.neoforged.api.distmarker.*;
import org.zeith.hammerlib.tiles.tooltip.own.IRenderableInfo;

public class TooltipInfoText
		implements IRenderableInfo
{
	@Getter
	@Setter
	protected MutableComponent text;
	
	public Font fontRenderer;
	
	@Setter
	public boolean dropShadow = true;
	
	public TooltipInfoText(Component text)
	{
		this.text = text instanceof MutableComponent mc ? mc : text.copy();
		this.fontRenderer = Minecraft.getInstance().font;
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
	public void render(GuiGraphics gfx, float x, float y, DeltaTracker partialTime)
	{
		gfx.drawString(fontRenderer, getText().getVisualOrderText(), x, y, 0xFFFFFFFF, dropShadow);
	}
}