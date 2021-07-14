package org.zeith.hammerlib.api.lighting.handlers;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.zeith.hammerlib.api.lighting.ColoredLight;

public interface ILightBlockHandler
{
	ColoredLight createLight(World world, BlockPos pos, BlockState state);

	default void update(BlockState state, BlockPos pos)
	{
	}
}