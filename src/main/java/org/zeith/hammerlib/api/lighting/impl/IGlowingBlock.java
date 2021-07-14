package org.zeith.hammerlib.api.lighting.impl;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.zeith.hammerlib.api.lighting.ColoredLight;

public interface IGlowingBlock
{
	ColoredLight produceColoredLight(World world, BlockPos pos, BlockState state, float partialTicks);
}