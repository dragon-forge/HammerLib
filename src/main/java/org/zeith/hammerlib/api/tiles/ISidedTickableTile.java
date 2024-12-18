package org.zeith.hammerlib.api.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

public interface ISidedTickableTile
		extends BlockEntityTicker
{
	void clientTick(Level level, BlockPos pos, BlockState state, BlockEntity be);
	
	void serverTick(ServerLevel level, BlockPos pos, BlockState state, BlockEntity be);
	
	@Override
	default void tick(Level level, BlockPos pos, BlockState state, BlockEntity be)
	{
		if(be != this) return;
		if(level.isClientSide) clientTick(level, pos, state, be);
		else if(level instanceof ServerLevel sl) serverTick(sl, pos, state, be);
	}
}