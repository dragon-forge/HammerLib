package com.pengu.hammercore.cfg;

import com.pengu.hammercore.cfg.fields.ModConfigPropertyBool;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

@HCModConfigurations(modid = "hammercore")
public class HammerCoreConfigs implements iConfigReloadListener
{
	@ModConfigPropertyBool(name = "Smooth Vanilla Guis", category = "Client", defaultValue = true, comment = "Replace vanilla furnace and brewing stand progress bars to use smooth rendering?")
	public static boolean client_smoothVanillaGuis = true;
	
	@ModConfigPropertyBool(name = "Mod Browser", category = "Client", defaultValue = true, comment = "Should Hammer Core add Mod Browser feature to main menu?")
	public static boolean client_modBrowser = true;
	
	@ModConfigPropertyBool(name = "LAN UPnP", category = "Client", defaultValue = true, comment = "Should Hammer Core portforward your local world when you share with it to LAN?\nThis feature doesn't work on all clients, you are going to get a message in chat if it was successful/unsuccessful")
	public static boolean client_useUPnP = true;
	
	@ModConfigPropertyBool(name = "Custom Enchantment Colors for Vanilla Items", category = "Client", defaultValue = true, comment = "Should Hammer Core replace enchantment glint color with more matching colors for some vanilla items?")
	public static boolean client_customDefEnchCols = true;
	
	@ModConfigPropertyBool(name = "Zapper", category = "Debug", defaultValue = false, comment = "Should Hammer Core add debug zapper item?")
	public static boolean debug_addZapper = false;
	
	@ModConfigPropertyBool(name = "Raytracer", category = "Debug", defaultValue = false, comment = "Should Hammer Core add debug ray tracer item?")
	public static boolean debug_addRaytracer = false;
	
	@ModConfigPropertyBool(name = "Always Spawn Dragon Egg", category = "Vanilla Improvements", defaultValue = true, comment = "Should Hammer Core force-spawn Ender Dragon Egg on Ender Dragon death?")
	public static boolean vanilla_alwaysSpawnDragonEggs = true;
	
	public static boolean CustomLANPortInstalled;
	
	public static Configuration cfg;
	
	@Override
	public void reloadCustom(Configuration cfgs)
	{
		CustomLANPortInstalled = Loader.isModLoaded("customports") || "@VERSION@".contains("VERSION");
		cfg = cfgs;
	}
}