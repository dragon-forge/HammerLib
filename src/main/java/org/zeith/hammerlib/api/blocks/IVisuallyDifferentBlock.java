package org.zeith.hammerlib.api.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * An interface for blocks that have visually different variants depending on their surroundings.
 */
public interface IVisuallyDifferentBlock
{
	/**
	 * Called when the block is being rendered to alter it's visual state.
	 *
	 * @param level The level the block is in.
	 * @param pos The position of the block in the world.
	 * @param state The current block state of the block.
	 * @return The block state that should be displayed for this block.
	 */
	default BlockState handle(Level level, BlockPos pos, BlockState state)
	{
		return state;
	}
}