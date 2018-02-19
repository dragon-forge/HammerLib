package com.pengu.hammercore.common;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

/**
 * This enum used to make machines that have only four possible directions.
 **/
public enum EnumRotation implements IStringSerializable
{
	SOUTH, NORTH, EAST, WEST;
	
	public static final PropertyEnum<EnumFacing> EFACING = PropertyEnum.create("facing", EnumFacing.class);
	
	public static final PropertyEnum<EnumRotation> FACING = PropertyEnum.create("facing", EnumRotation.class);
	public static final PropertyEnum<EnumRotation> ROTATION = PropertyEnum.create("rotation", EnumRotation.class);
	
	private EnumRotation()
	{
	}
	
	@Override
	public String getName()
	{
		return name().toLowerCase();
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
}