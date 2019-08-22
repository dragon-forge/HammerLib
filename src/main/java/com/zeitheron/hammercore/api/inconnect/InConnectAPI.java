package com.zeitheron.hammercore.api.inconnect;

import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class InConnectAPI
{
	public static final BiFunction<IBlockAccess, Pair<BlockPos, IBlockState>, IBlockState> extendedState = (w, p) -> p.getValue();
	
	public static IBlockState makeExtendedPositionedState(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return extendedState.apply(world, Pair.of(pos, state));
	}
}