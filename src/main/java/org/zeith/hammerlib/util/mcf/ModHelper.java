package org.zeith.hammerlib.util.mcf;

import net.minecraftforge.fml.ModList;

public class ModHelper
{
	public static String getModVersion(String modid)
	{
		StringBuilder version = new StringBuilder();
		ModList.get().getModContainerById(modid).ifPresent(mc -> version.append(mc.getModInfo().getVersion().toString()));
		return version.toString();
	}
}
