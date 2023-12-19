package org.zeith.hammerlib.tiles.tooltip.own.inf;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.neoforged.api.distmarker.*;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.tiles.tooltip.ProgressBar;
import org.zeith.hammerlib.tiles.tooltip.own.IRenderableInfo;

public record TooltipInfoProgressBar(ProgressBar bar)
		implements IRenderableInfo
{
	@Override
	public float getWidth()
	{
		return 101;
	}
	
	@Override
	public float getHeight()
	{
		return 12;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(GuiGraphics gfx, float x, float y, float partialTime)
	{
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		
		var pose = gfx.pose();
		
		pose.pushPose();
		pose.translate(x, y, 0);
		
		RenderUtils.drawColoredModalRect(gfx, 0, 0, 101, 12, bar.borderColor);
		RenderUtils.drawColoredModalRect(gfx, 1, 1, 99, 10, bar.backgroundColor);
		
		int fill = bar.getProgressPercent();
		
		RenderUtils.drawColoredModalRect(gfx, 1, 1, fill, 10, bar.filledMainColor);
		
		for(int j = 0; j < fill; j += 2)
			RenderUtils.drawColoredModalRect(gfx, 1 + j, 1, 1, 10, bar.filledAlternateColor);
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		gfx.drawString(Minecraft.getInstance().font, (bar.prefix != null ? bar.prefix : "") + (bar.suffix != null ? bar.suffix : ""), 3, 2, 0xFFFFFF);
		
		pose.popPose();
	}
}