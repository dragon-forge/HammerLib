package org.zeith.hammerlib.tiles.tooltip.own.inf;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.zeith.hammerlib.tiles.tooltip.own.IRenderableInfo;

public record TooltipInfoStack(ItemStack stack, float width, float height)
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
	public void render(GuiGraphics matrix, float x, float y, DeltaTracker partialTime)
	{
		var pose = matrix.pose();
		
		pose.pushPose();
		pose.translate(x, y, 0);
		pose.scale(width / 16F, height / 16F, 1);
		matrix.renderItem(stack, 0, 0);
		pose.popPose();
	}
}