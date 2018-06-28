package com.zeitheron.hammercore.client.utils;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;

public enum GLRenderState
{
	BLEND, //
	STANDART_ITEM_LIGHTING, //
	STANDART_GUI_LIGHTING;
	
	private boolean isEnabled = false;
	
	private boolean isOn()
	{
		switch(this)
		{
		case BLEND:
			return GL11.glIsEnabled(GL11.GL_BLEND);
		case STANDART_ITEM_LIGHTING:
			return true;
		case STANDART_GUI_LIGHTING:
			return false;
		}
		
		return false;
	}
	
	public boolean captureState()
	{
		return isEnabled = isOn();
	}
	
	public void on()
	{
		switch(this)
		{
		case BLEND:
			GlStateManager.enableBlend();
		case STANDART_ITEM_LIGHTING:
			RenderHelper.enableStandardItemLighting();
		case STANDART_GUI_LIGHTING:
			RenderHelper.enableGUIStandardItemLighting();
		}
	}
	
	public void off()
	{
		switch(this)
		{
		case BLEND:
			GlStateManager.disableBlend();
		case STANDART_ITEM_LIGHTING:
			RenderHelper.disableStandardItemLighting();
		case STANDART_GUI_LIGHTING:
			RenderHelper.disableStandardItemLighting();
		}
	}
	
	public void reset()
	{
		if(isEnabled)
			on();
		else
			off();
	}
}