package com.pengu.hammercore.client.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public interface iTexBindable
{
	void bind();
	
	static iTexBindable ofGLTex(int tex)
	{
		return () -> GlStateManager.bindTexture(tex);
	}
	
	static iTexBindable ofMCTex(ResourceLocation tex)
	{
		return () -> Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
	}
}