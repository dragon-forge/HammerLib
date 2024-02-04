package com.zeitheron.hammercore.utils.forge;

import net.minecraftforge.fml.common.*;

public class ModHelper
{
	public static boolean isModLoaded(String modid)
	{
		return Loader.isModLoaded(modid);
	}
	
	public static String getModVersion(String modid)
	{
		StringBuilder version = new StringBuilder();
		ModContainer mc = Loader.instance().getIndexedModList().get(modid);
		if(mc != null) version.append(mc.getVersion());
		return version.toString();
	}
}
