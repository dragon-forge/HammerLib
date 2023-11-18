package com.zeitheron.hammercore;

import net.minecraft.util.ResourceLocation;

public class HLConstants
{
	public static final String MODID = "hammercore";
	
	public static ResourceLocation id(String path)
	{
		return new ResourceLocation(MODID, path);
	}
}