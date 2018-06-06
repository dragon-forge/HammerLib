package com.pengu.hammercore.client.witty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SplashModPool
{
	private static Map<String, String> mods = new HashMap<>();
	
	static
	{
		link("botania", "Botania");
		link("jei", "Just Enough Items");
		link("waila", "WAILA");
		link("solarfluxreborn", "Solar Flux Reborn");
		link("thaumcraft", "Thaumcraft VI");
		link("embers", "Embers");
		link("astralsorcery", "Astral Sorcery");
		link("ic2", "Industrial Craft");
		link("actuallyadditions", "Actually Additions");
	}
	
	public static void link(String modid, String name)
	{
		mods.put(modid, name);
	}
	
	public static Collection<String> modIds()
	{
		return mods.keySet();
	}
	
	public static String getName(String modid)
	{
		return mods.get(modid);
	}
}