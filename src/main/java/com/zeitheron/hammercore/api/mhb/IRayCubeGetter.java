package com.zeitheron.hammercore.api.mhb;

import javax.annotation.Nullable;

import com.zeitheron.hammercore.utils.WorldUtil;
import com.zeitheron.hammercore.utils.math.vec.Cuboid6;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Registry class that allows modder to get hitboxes for a
 * {@link BlockTraceable}
 */
public interface IRayCubeGetter
{
	/**
	 * Gets a current instance of {@link IRayCubeGetter}
	 */
	static IRayCubeGetter instance()
	{
		return Instance.getter;
	}
	
	/**
	 * Gets an array of hard-coded cuboids for a block
	 */
	public Cuboid6[] getBoundCubes6(BlockTraceable target);
	
	/**
	 * Gets a manager for retrieving cuboids based on world and position (used
	 * for dynamic cuboids like cables)
	 */
	public ICubeManager getBoundCubeManager(BlockTraceable target);
	
	/**
	 * Gets an array of cuboids found for the block at given location
	 */
	@Nullable
	default Cuboid6[] getCuboids(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		BlockTraceable block = WorldUtil.cast(state.getBlock(), BlockTraceable.class);
		if(block != null)
		{
			ICubeManager mgr = getBoundCubeManager(block);
			if(mgr != null)
				return mgr.getCuboids(world, pos, state);
			else
				return getBoundCubes6(block);
		}
		return null;
	}
	
	public static class Instance
	{
		public static IRayCubeGetter getter;
	}
}