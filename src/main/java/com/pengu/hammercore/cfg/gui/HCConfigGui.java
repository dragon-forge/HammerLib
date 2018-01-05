package com.pengu.hammercore.cfg.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.pengu.hammercore.cfg.HammerCoreConfigs;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class HCConfigGui extends GuiConfig
{
	/**
	 * Must-have constructor for making gui factories
	 */
	public HCConfigGui(GuiScreen prev)
	{
		this(prev, HammerCoreConfigs.cfg, "hammercore");
	}
	
	public HCConfigGui(GuiScreen prev, Configuration cfg, String modid)
	{
		super(prev, getElements(cfg), modid, allRequireWorldRestart(cfg), allRequireMcRestart(cfg), getAbridgedConfigPath(cfg.toString()));
		ModContainer mc = getContainerByID(modid);
		title = "Config GUI for " + (mc != null ? mc.getName() : "Unknown mod");
		File configs = new File("config");
		File c = new File(cfg.toString());
		titleLine2 = c.getAbsolutePath().substring(configs.getAbsolutePath().length() + 1);
	}
	
	public static ModContainer getContainerByID(String modid)
	{
		for(ModContainer mc : Loader.instance().getActiveModList())
			if(mc.getModId().equals(modid))
				return mc;
		return null;
	}
	
	public static boolean allRequireWorldRestart(Configuration cfg)
	{
		for(String cat : cfg.getCategoryNames())
			if(!cfg.getCategory(cat).requiresWorldRestart())
				return false;
		return true;
	}
	
	public static boolean allRequireMcRestart(Configuration cfg)
	{
		for(String cat : cfg.getCategoryNames())
			if(!cfg.getCategory(cat).requiresMcRestart())
				return false;
		return true;
	}
	
	public static List<IConfigElement> getElements(Configuration cfg)
	{
		List<IConfigElement> a = new ArrayList<>();
		for(String cat : cfg.getCategoryNames())
			a.add(new ConfigElement(cfg.getCategory(cat)));
		return a;
	}
}