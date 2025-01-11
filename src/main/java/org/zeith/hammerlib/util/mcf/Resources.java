package org.zeith.hammerlib.util.mcf;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class Resources
{
	public static ResourceLocation location(String namespace, String path)
	{
		return ResourceLocation.fromNamespaceAndPath(namespace, path);
	}
	
	public static ResourceLocation location(String location)
	{
		return ResourceLocation.parse(location);
	}
	
	@Nullable
	public static ResourceLocation locationOrNull(String namespace, String path)
	{
		return ResourceLocation.tryBuild(namespace, path);
	}
	
	@Nullable
	public static ResourceLocation locationOrNull(String location)
	{
		if(location == null) return null;
		try
		{
			return ResourceLocation.tryParse(location);
		} catch(Exception e)
		{
			return null;
		}
	}
}