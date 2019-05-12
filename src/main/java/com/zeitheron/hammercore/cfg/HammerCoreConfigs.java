package com.zeitheron.hammercore.cfg;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.cfg.fields.ModConfigPropertyBool;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

/**
 * An internal class. Use it as an example of how to make custom configs with
 * Hammer Core.
 */
@HCModConfigurations(modid = "hammercore")
public class HammerCoreConfigs implements IConfigReloadListener
{
	public static HammerCoreConfigs configInstance;
	{
		configInstance = this;
	}
	
	@ModConfigPropertyBool(name = "Smooth Vanilla Guis", category = "Client", defaultValue = true, comment = "Replace vanilla furnace and brewing stand progress bars to use smooth rendering?")
	public static boolean client_smoothVanillaGuis = true;
	
	@ModConfigPropertyBool(name = "LAN UPnP", category = "Client", defaultValue = true, comment = "Should Hammer Core portforward your local world when you share with it to LAN?\nThis feature doesn't work on all clients, you are going to get a message in chat if it was successful/unsuccessful")
	public static boolean client_useUPnP = true;
	
	@ModConfigPropertyBool(name = "Custom Enchantment Colors for Vanilla Items", category = "Client", defaultValue = true, comment = "Should Hammer Core replace enchantment glint color with more matching colors for some vanilla items?")
	public static boolean client_customDefEnchCols = true;
	
	@ModConfigPropertyBool(name = "Always Spawn Dragon Egg", category = "Vanilla Improvements", defaultValue = true, comment = "Should Hammer Core force-spawn Ender Dragon Egg on Ender Dragon death?")
	public static boolean vanilla_alwaysSpawnDragonEggs = true;
	
	public static int iwr_green, iwr_red;
	
	public static Configuration cfg;
	
	@Override
	public void reloadCustom(Configuration cfgs)
	{
		cfg = cfgs;
		
		Property prop = cfgs.get("iWrench", "Green", "22FF22");
		prop.setComment("What is the color when the hovered block is wrenchable? (color encoded in hex form such as RRGGBB)");
		
		try
		{
			iwr_green = Integer.parseInt(prop.getString(), 16);
		} catch(Throwable err)
		{
			HammerCore.LOG.warn("Unable to parse green iWrench color! Setting to default!");
			prop.set("22FF22");
		}
		
		prop = cfgs.get("iWrench", "Red", "FF2222");
		prop.setComment("What is the color when the hovered block is NOT wrenchable? (color encoded in hex form such as RRGGBB)");
		
		try
		{
			iwr_red = Integer.parseInt(prop.getString(), 16);
		} catch(Throwable err)
		{
			HammerCore.LOG.warn("Unable to parse red iWrench color! Setting to default!");
			prop.set("FF2222");
		}
	}
}