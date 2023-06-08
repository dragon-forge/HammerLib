package org.zeith.hammerlib.tiles.tooltip.own.inf;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.tiles.tooltip.own.IRenderableInfo;

public record TooltipInfoSpacing(float width, float height)
		implements IRenderableInfo
{
	@Override
	public float getWidth()
	{
		return width;
	}
	
	@Override
	public float getHeight()
	{
		return height;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(GuiGraphics matrix, float x, float y, float partialTime)
	{
	}
}