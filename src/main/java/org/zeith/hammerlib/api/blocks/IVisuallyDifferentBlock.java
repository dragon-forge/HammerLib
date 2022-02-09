package org.zeith.hammerlib.api.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IVisuallyDifferentBlock
{
	default BlockState handle(Level level, BlockPos pos, BlockState state)
	{
		return state;
	}
}