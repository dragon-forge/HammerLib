package org.zeith.hammerlib.api.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Interface for blocks that alter their hit position based on the current block state.
 */
public interface IHitsDifferentTargetBlock
{
	/**
	 * Alters the hit position of the block based on the current block state.
	 *
	 * @param level
	 * 		The level the block is in.
	 * @param pos
	 * 		The current hit position of the block.
	 * @param state
	 * 		The current block state.
	 *
	 * @return The altered hit position of the block.
	 */
	BlockPos alterHitPosition(Level level, BlockPos pos, BlockState state);
}