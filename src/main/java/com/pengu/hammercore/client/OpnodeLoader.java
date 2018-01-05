package com.pengu.hammercore.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pengu.hammercore.client.model.simple.SimpleModelParser;
import com.pengu.hammercore.common.utils.IOUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class OpnodeLoader
{
	private static Map<String, List<int[]>> loaded = new HashMap<>();
	
	public static List<int[]> loadOpnodes(String modid, String file)
	{
		if(loaded.get(modid + ":" + file) != null)
			return loaded.get(modid + ":" + file);
		
		try
		{
			List<int[]> nodes = SimpleModelParser.toOpcodes(new String(IOUtils.pipeOut(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(modid, "models/" + file + ".hcmf")).getInputStream())));
			loaded.put(modid + ":" + file, nodes);
			return nodes;
		} catch(Throwable err)
		{
		}
		
		return Arrays.asList();
	}
	
	public static void reloadModel(String modid, String file)
	{
		loaded.remove(modid + ":" + file);
	}
	
	public static void reloadModels()
	{
		loaded.clear();
	}
}