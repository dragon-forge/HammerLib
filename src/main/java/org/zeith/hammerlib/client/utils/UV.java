package org.zeith.hammerlib.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.render.IGuiDrawable;

public class UV
		implements IGuiDrawable
{
	public ResourceLocation path;
	public float posX, posY, width, height;
	
	public int txWidth = 256, txHeight = 256;
	
	public UV(ResourceLocation resource, float x, float y, float w, float h)
	{
		posX = x;
		posY = y;
		width = w;
		height = h;
		path = resource;
	}
	
	@OnlyIn(Dist.CLIENT)
	public void render(GuiGraphics gfx, double x, double y)
	{
		render(gfx, x, y, width, height);
	}
	
	@OnlyIn(Dist.CLIENT)
	public void render(GuiGraphics gfx, double x, double y, float width, float height)
	{
		var pose = gfx.pose();
		pose.pushPose();
		pose.translate(x, y, 0);
		pose.scale((1F / this.width) * width, (1F / this.height) * height, 1F);
		doRender(gfx, ARGB.white(1F));
		pose.popPose();
	}
	
	@OnlyIn(Dist.CLIENT)
	public void renderWithColor(GuiGraphics gfx, int color, double x, double y, float width, float height)
	{
		var pose = gfx.pose();
		pose.pushPose();
		pose.translate(x, y, 0);
		pose.scale((1F / this.width) * width, (1F / this.height) * height, 1F);
		doRender(gfx, color);
		pose.popPose();
	}
	
	@OnlyIn(Dist.CLIENT)
	protected void doRender(GuiGraphics gfx, int color)
	{
		var g = Graphics.builder().gfx(gfx).game(Minecraft.getInstance()).build();
		g.blit(RenderType::guiTextured, getTexture(),
				0, 0,
				posX, posY,
				width, height,
				width, height,
				txWidth, txHeight,
				color
		);
	}
	
	@OnlyIn(Dist.CLIENT)
	public ResourceLocation getTexture()
	{
		return path;
	}
	
	@Override
	public String toString()
	{
		return "UV{" +
			   "path=" + path +
			   ", posX=" + posX +
			   ", posY=" + posY +
			   ", width=" + width +
			   ", height=" + height +
			   '}';
	}
	
	@Override
	public void renderDrawable(GuiGraphics gfx, int xOffset, int yOffset, int width, int height)
	{
		render(gfx, xOffset, yOffset, width, height);
	}
}