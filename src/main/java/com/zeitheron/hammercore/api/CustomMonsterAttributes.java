package com.zeitheron.hammercore.api;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

/**
 * Few custom attributes that Hammer Core implements for use.
 */
public class CustomMonsterAttributes
{
	public static final IAttribute FLIGHT_SPEED = new RangedAttribute(null, "generic.flightSpeed", 0, 0D, 128D).setDescription("Flight Speed").setShouldWatch(true);
	public static final IAttribute WALK_SPEED = new RangedAttribute(null, "generic.walkSpeed", 0, 0D, 128D).setDescription("Walk Speed").setShouldWatch(true);
}