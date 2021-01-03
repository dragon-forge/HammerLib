package com.zeitheron.hammercore.utils;

import net.minecraft.world.World;

public enum EnumMoonPhase
{
	FULL,
	WANING_GIBBOUS,
	LAST_QUARTER,
	WANING_CRESCENT,
	NEW_MOON,
	WAXING_CRESCENT,
	FIRST_QUARTER,
	WAXING_GIBBOUS;

	static int phase(World world)
	{
		return (int) ((world.getWorldTime() / 24000L % 8L + 8L) % 8);
	}

	private static final EnumMoonPhase[] PHASES = EnumMoonPhase.values();

	public static EnumMoonPhase getMoonPhase(World world)
	{
		return PHASES[phase(world) % PHASES.length];
	}
}