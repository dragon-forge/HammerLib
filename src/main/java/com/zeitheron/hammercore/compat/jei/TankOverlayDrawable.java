package com.zeitheron.hammercore.compat.jei;

import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.client.gui.GuiTankTexture;
import com.zeitheron.hammercore.client.utils.RenderUtil;

import mezz.jei.api.gui.IDrawable;
import net.minecraft.client.Minecraft;

public class TankOverlayDrawable implements IDrawable
{
	public final GuiTankTexture texture;
	
	public TankOverlayDrawable(GuiTankTexture texture)
	{
		this.texture = texture;
	}
	
	@Override
	public int getWidth()
	{
		return texture.getWidth();
	}

	@Override
	public int getHeight()
	{
		return texture.getHeight();
	}

	@Override
	public void draw(Minecraft minecraft, int xOffset, int yOffset)
	{
		texture.draw(xOffset, yOffset);
	}
}