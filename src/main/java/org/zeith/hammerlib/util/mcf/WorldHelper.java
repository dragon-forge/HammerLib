package org.zeith.hammerlib.util.mcf;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class WorldHelper
{
	public static ResourceLocation getWorldId(World world)
	{
		return world.dimension().getRegistryName();
	}
}