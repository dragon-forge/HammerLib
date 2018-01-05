package com.pengu.hammercore.client.particle;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public interface iRenderHelper
{
	ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
	
	default void renderEndPortalEffect(double x, double y, double z, EnumFacing... renderSides)
	{
		renderEndPortalEffect(x, y, z, END_PORTAL_TEXTURE, renderSides);
	}
	
	void renderEndPortalEffect(double x, double y, double z, ResourceLocation end_portal_texture, EnumFacing... renderSides);
}