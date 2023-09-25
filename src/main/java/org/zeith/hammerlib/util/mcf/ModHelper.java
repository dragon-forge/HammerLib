package org.zeith.hammerlib.util.mcf;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ModHelper
{
	public static boolean inDev()
	{
		return !FMLEnvironment.production;
	}
	
	public static boolean isDedicatedServer()
	{
		return FMLEnvironment.dist == Dist.DEDICATED_SERVER;
	}
	
	public static boolean isClient()
	{
		return FMLEnvironment.dist == Dist.CLIENT;
	}
	
	public static boolean isModLoaded(String modid)
	{
		return ModList.get().isLoaded(modid);
	}
	
	public static String getModVersion(String modid)
	{
		StringBuilder version = new StringBuilder();
		ModList.get().getModContainerById(modid)
				.ifPresent(mc -> version.append(mc.getModInfo().getVersion().toString()));
		return version.toString();
	}
}
