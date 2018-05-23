package com.pengu.hammercore.common.blocks.base;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.EnumFacing.Axis;

public interface iBlockHorizontal
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", f -> f.getAxis() != Axis.Y);
}