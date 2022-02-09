package org.zeith.hammerlib.api.lighting.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.zeith.hammerlib.api.lighting.ColoredLight;

public interface ILightBlockHandler
{
	ColoredLight createLight(Level level, BlockPos pos, BlockState state);

	default void update(BlockState state, BlockPos pos)
	{
	}
}