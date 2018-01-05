package com.pengu.hammercore.cfg;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public interface iConfigReloadListener
{
	default void reloadCustom(Configuration cfgs)
	{
	}
	
	default String getModid()
	{
		return getClass().getAnnotation(HCModConfigurations.class).modid();
	}
	
	default File getSuggestedConfigurationFile()
	{
		return new File("config", getModid() + ".cfg");
	}
}