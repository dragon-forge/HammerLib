package com.pengu.hammercore.tile.tooltip.own.inf;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.tile.tooltip.own.IRenderableInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class ItemStackTooltipInfo implements IRenderableInfo
{
	public ItemStack stack;
	public int width, height;
	
	public ItemStackTooltipInfo(ItemStack stack, int width, int height)
	{
		this.stack = stack;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public int getWidth()
	{
		return this.width;
	}
	
	@Override
	public int getHeight()
	{
		return this.height;
	}
	
	@Override
	public void render(float x, float y, float partialTime)
	{
		RenderHelper.enableGUIStandardItemLighting();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GL11.glScaled(width / 16., height / 16., 1);
		Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
		GL11.glPopMatrix();
	}
}