package com.zeitheron.hammercore.bookAPI;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BookRenderingHelper
{
	public static Minecraft getMinecraft()
	{
		return Minecraft.getMinecraft();
	}
	
	public static void bindTexture(ResourceLocation location)
	{
		getMinecraft().getTextureManager().bindTexture(location);
	}
}