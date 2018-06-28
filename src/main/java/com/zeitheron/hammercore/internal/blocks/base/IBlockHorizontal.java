package com.zeitheron.hammercore.internal.blocks.base;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.EnumFacing.Axis;

public interface IBlockHorizontal
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", f -> f.getAxis() != Axis.Y);
}