package org.zeith.hammerlib.util.mcf;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class Resources
{
	public static ResourceLocation location(String namespace, String path)
	{
		return new ResourceLocation(namespace, path);
	}
	
	public static ResourceLocation location(String location)
	{
		return new ResourceLocation(location);
	}
	
	@Nullable
	public static ResourceLocation locationOrNull(String namespace, String path)
	{
		return ResourceLocation.tryBuild(namespace, path);
	}
	
	@Nullable
	public static ResourceLocation locationOrNull(String location)
	{
		return ResourceLocation.tryParse(location);
	}
}