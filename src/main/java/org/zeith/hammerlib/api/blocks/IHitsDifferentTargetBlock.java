package org.zeith.hammerlib.api.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IHitsDifferentTargetBlock
{
	BlockPos alterHitPosition(Level level, BlockPos pos, BlockState state);
}