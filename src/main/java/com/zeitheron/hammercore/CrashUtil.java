package com.zeitheron.hammercore;

import java.nio.file.Files;

import com.zeitheron.hammercore.cfg.HammerCoreConfigs;
import com.zeitheron.hammercore.fluiddict.FluidDictionary;
import com.zeitheron.hammercore.lib.zlib.web.HttpRequest;
import com.zeitheron.hammercore.lib.zlib.web.HttpRequest.Base64;

import net.minecraftforge.fml.common.ICrashCallable;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.oredict.OreDictionary;

class CrashUtil implements ICrashCallable
{
	@Override
	public String call() throws Exception
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("FluidDictionary Info: Registered " + FluidDictionary.getFluidNames().length + " fluids.\n");
		
		int items = 0;
		for(String name : OreDictionary.getOreNames())
			items += OreDictionary.getOres(name).size();
		sb.append("OreDictionary Info: Registered " + OreDictionary.getOreNames().length + " names; " + items + " stacks.\n");
		
		if(HammerCoreConfigs.configInstance != null)
			sb.append("Configuration Info: " + Base64.encodeBytes(Files.readAllBytes(HammerCoreConfigs.configInstance.getSuggestedConfigurationFile().toPath())) + "\n");
		
		sb.append("Pastebin Connection: ");
		try
		{
			String text = HttpRequest.get("https://pastebin.com/raw/ZQaapJ54").connectTimeout(500).body();
			if(!text.startsWith("["))
				sb.append("Failed!");
			sb.append("Okay.\n");
		} catch(Throwable err)
		{
			sb.append("Failed!\n");
		}
		
		sb.append("Dependent Mods:");
		
		int f = 0;
		withMods: for(ModContainer mc : Loader.instance().getActiveModList())
			for(ArtifactVersion av : mc.getRequirements())
				if(av.getLabel().equals("hammercore"))
				{
					sb.append("\n    -" + mc.getName() + " (" + mc.getModId() + ") @" + mc.getVersion());
					++f;
					continue withMods;
				}
		
		if(f == 0)
			sb.append(" None.");
		
		sb.append("\n");
			
		return sb.toString();
	}
	
	@Override
	public String getLabel()
	{
		return "Hammer Core Information";
	}
}