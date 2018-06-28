package com.zeitheron.hammercore.api.mhb;

import com.zeitheron.hammercore.utils.math.vec.Cuboid6;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * An advanced way of getting hitboxes for {@link BlockTraceable}
 */
public interface ICubeManager
{
	public Cuboid6[] getCuboids(World world, BlockPos pos, IBlockState state);
}