package org.zeith.hammerlib.api.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IVisuallyDifferentBlock
{
	default BlockState handle(World world, BlockPos pos, BlockState state)
	{
		return state;
	}
}