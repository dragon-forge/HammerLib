package com.pengu.hammercore.api.dynlight;

import com.pengu.hammercore.HammerCore;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class ProxiedDynlightGetter
{
	public static int getLightValue(IBlockState blockState, IBlockAccess world, BlockPos pos)
	{
		int vanillaValue = blockState.getLightValue(world, pos);
		int custom = HammerCore.particleProxy.getLightValue(blockState, world, pos);
		return Math.max(vanillaValue, custom);
	}
}