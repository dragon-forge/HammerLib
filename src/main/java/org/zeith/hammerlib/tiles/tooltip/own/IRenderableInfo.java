package org.zeith.hammerlib.tiles.tooltip.own;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IRenderableInfo
{
	float getWidth();
	
	float getHeight();
	
	@OnlyIn(Dist.CLIENT)
	void render(GuiGraphics matrix, float x, float y, float partialTime);
}