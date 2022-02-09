package org.zeith.hammerlib.api.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityParams
{
	public final BlockPos pos;
	public final BlockState state;

	public BlockEntityParams(BlockPos pos, BlockState state)
	{
		this.pos = pos;
		this.state = state;
	}
}