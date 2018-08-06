package com.zeitheron.hammercore.cfg;

import java.io.File;

import com.zeitheron.hammercore.cfg.fields.ModConfigPropertyBool;
import com.zeitheron.hammercore.cfg.fields.ModConfigPropertyFloat;
import com.zeitheron.hammercore.cfg.fields.ModConfigPropertyInt;
import com.zeitheron.hammercore.cfg.fields.ModConfigPropertyString;
import com.zeitheron.hammercore.cfg.fields.ModConfigPropertyStringList;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Makes this class a configuration class. Must
 * have @{@link HCModConfigurations} to function. All fields in the class that
 * have either one of these: <br>
 * {@link ModConfigPropertyBool} {@link ModConfigPropertyFloat}
 * {@link ModConfigPropertyInt} {@link ModConfigPropertyString} or
 * {@link ModConfigPropertyStringList} <br>
 * will be pulled from a config file and set at
 * {@link FMLPreInitializationEvent}. <br>
 * Tip: use "required-after:hammercore@[VERSION,)" in your
 * {@link Mod#dependencies()}, where VERSION is the version of Hammer Core that
 * you are building for. Recommended to ALWAYS use latest versions.
 */
public interface IConfigReloadListener
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