package org.zeith.hammerlib.client.render;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.util.java.Cast;

import java.util.function.Supplier;

@FunctionalInterface
public interface IGuiDrawable
{
	IGuiDrawable EMPTY = (gfx, xOffset, yOffset, width, height) ->
	{
	};
	
	void renderDrawable(GuiGraphics gfx, int xOffset, int yOffset, int width, int height);
	
	static IGuiDrawable ofItem(Supplier<ItemStack> stack)
	{
		return (gfx, xOffset, yOffset, width, height) ->
		{
			var pose = gfx.pose();
			pose.pushPose();
			pose.translate(xOffset, yOffset, 0);
			pose.scale(width * 0.0625F, height * 0.0625F, width * 0.0625F);
			gfx.renderItem(stack.get(), 0, 0);
			pose.popPose();
		};
	}
	
	static IGuiDrawable ofItem(ItemStack stack)
	{
		return ofItem(Cast.constant(stack));
	}
}