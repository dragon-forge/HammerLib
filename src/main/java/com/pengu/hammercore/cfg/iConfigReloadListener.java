package com.pengu.hammercore.cfg;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

public interface iConfigReloadListener
{
	default void reloadCustom(Configuration cfgs)
	{
	}
	
	default HCModConfigurations getHCMC()
	{
		return getClass().getAnnotation(HCModConfigurations.class);
	}
	
	default String getModid()
	{
		return getHCMC().modid();
	}
	
	default String getModule()
	{
		return getHCMC().isModule() ? getHCMC().module() : "";
	}
	
	default File getSuggestedConfigurationFile()
	{
		String sub = getModule();
		if(!sub.isEmpty())
		{
			File f = new File(Loader.instance().getConfigDir(), getModid());
			if(!f.isDirectory())
				f.mkdirs();
			return new File(f, sub + ".cfg");
		}
		return new File(Loader.instance().getConfigDir(), getModid() + ".cfg");
	}
}