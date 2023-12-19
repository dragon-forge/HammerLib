package org.zeith.hammerlib.tiles.tooltip.own.inf;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.*;
import org.zeith.hammerlib.client.utils.RenderUtils;
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
	public void render(GuiGraphics matrix, float x, float y, float partialTime)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		
		var pose = matrix.pose();
		
		pose.pushPose();
		pose.translate(x, y, 0);
		pose.scale(width / 16F, height / 16F, 1);
		RenderUtils.renderItemIntoGui(pose, stack, 0F, 0F);
		pose.popPose();
	}
}