package com.zeitheron.hammercore.client.utils;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UV
{
	public ResourceLocation path;
	public double posX, posY, width, height;
	
	public UV(ResourceLocation resource, double x, double y, double w, double h)
	{
		posX = x;
		posY = y;
		width = w;
		height = h;
		path = resource;
	}
	
	public void render(double x, double y, double width, double height)
	{
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, 0F);
		GL11.glScaled((1F / this.width) * width, (1F / this.height) * height, 1F);
		render(0, 0);
		GL11.glPopMatrix();
	}
	
	public void render(double x, double y)
	{
		boolean b = GL11.glIsEnabled(GL11.GL_BLEND);
		if(!b)
			GL11.glEnable(GL11.GL_BLEND);
		bindTexture();
		RenderUtil.drawTexturedModalRect(x, y, posX, posY, width, height);
		if(!b)
			GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void bindTexture()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(path);
	}
	
	@Override
	public String toString()
	{
		return getClass().getName() + "@[path=" + path + ",x=" + posX + ",y=" + posY + ",width=" + width + ",height=" + height + "]";
	}
}