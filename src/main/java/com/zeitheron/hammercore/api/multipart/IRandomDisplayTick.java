package com.zeitheron.hammercore.api.multipart;

import java.util.Random;

import net.minecraft.block.Block;

/**
 * Indicates that this {@link MultipartSignature} randomly emits particles.
 */
public interface IRandomDisplayTick
{
	/**
	 * Called periodically clientside on blocks near the player to show effects
	 * (like furnace fire particles). <br>
	 * Called similar to
	 * {@link Block#randomDisplayTick(net.minecraft.block.state.IBlockState, net.minecraft.world.World, net.minecraft.util.math.BlockPos, Random)}
	 */
	void randomDisplayTick(Random rand);
}