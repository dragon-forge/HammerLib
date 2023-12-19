package org.zeith.hammerlib.tiles.tooltip.own;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.*;

public interface IRenderableInfo
{
	float getWidth();
	
	float getHeight();
	
	@OnlyIn(Dist.CLIENT)
	void render(GuiGraphics matrix, float x, float y, float partialTime);
}