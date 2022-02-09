package org.zeith.hammerlib.util.mcf;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class WorldHelper
{
	public static ResourceLocation getLevelId(Level world)
	{
		return world.dimension().location();
	}
}