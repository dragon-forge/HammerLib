package org.zeith.hammerlib.api.lighting.impl;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.zeith.hammerlib.api.lighting.ColoredLight;

public interface IGlowingBlock
{
	ColoredLight produceColoredLight(Level level, BlockPos pos, BlockState state, float partialTicks);
}