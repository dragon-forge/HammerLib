package com.pengu.hammercore.asm;

import net.minecraft.world.World;

public class WorldHooks
{
	public static int getMoonPhase(World world)
	{
		return world.provider.getMoonPhase(world.getTotalWorldTime());
	}
	
	public static EnumMoonPhase getEMoonPhase(World world)
	{
		return EnumMoonPhase.values()[getMoonPhase(world) % EnumMoonPhase.values().length];
	}
}