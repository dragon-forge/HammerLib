package org.zeith.hammerlib.core.items.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import org.zeith.hammerlib.api.items.tooltip.TooltipColoredLine;
import org.zeith.hammerlib.client.utils.RenderUtils;

public class ClientTooltipColoredLine
		implements ClientTooltipComponent
{
	private final TooltipColoredLine colors;
	
	public ClientTooltipColoredLine(TooltipColoredLine colors)
	{
		this.colors = colors;
	}
	
	@Override
	public int getHeight(Font pFont)
	{
		return 1;
	}
	
	@Override
	public int getWidth(Font font)
	{
		return colors.getColors().size() / 2;
	}
	
	@Override
	public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics gfx)
	{
		var pose = gfx.pose();
		
		pose.pushPose();
		for(int color : colors.getColors())
		{
			RenderUtils.drawColoredModalRect(gfx, x, y, 0.5F, 1, color);
			pose.translate(0.5F, 0, 0);
		}
		pose.popPose();
	}
}