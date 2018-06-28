package com.zeitheron.hammercore.client.utils.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

public interface iTexBindable
{
	int getGLId();
	
	default void bind()
	{
		GlStateManager.bindTexture(getGLId());
	}
	
	default void delete()
	{
		GlStateManager.deleteTexture(getGLId());
	}
	
	static iTexBindable ofGLTex(int tex)
	{
		return () -> tex;
	}
	
	static iTexBindable ofMCTex(ResourceLocation tex)
	{
		ITextureObject itextureobject = Minecraft.getMinecraft().getTextureManager().mapTextureObjects.get(tex);
		
		if(itextureobject == null)
		{
			itextureobject = new SimpleTexture(tex);
			Minecraft.getMinecraft().getTextureManager().loadTexture(tex, itextureobject);
		}
		
		int gl = itextureobject.getGlTextureId();
		
		return () -> gl;
	}
}