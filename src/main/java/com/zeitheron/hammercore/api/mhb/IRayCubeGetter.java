package com.zeitheron.hammercore.api.mhb;

import com.zeitheron.hammercore.utils.base.Cast;
import com.zeitheron.hammercore.utils.math.vec.Cuboid6;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Registry class that allows modder to get hitboxes for a
 * {@link BlockTraceable}
 */
public interface IRayCubeGetter
{
	/**
	 * @return current instance of {@link IRayCubeGetter}
	 */
	static IRayCubeGetter instance()
	{
		return Instance.getter;
	}
	
	/**
	 * @param target
	 *            The target block
	 * @return an array of hard-coded cuboids for a block
	 */
	Cuboid6[] getBoundCubes6(BlockTraceable target);
	
	/**
	 * @param target
	 *            The target block
	 * @return a manager for retrieving cuboids based on world and position
	 *         (used for dynamic cuboids like cables)
	 */
	ICubeManager getBoundCubeManager(BlockTraceable target);
	
	/**
	 * @param world
	 *            The world
	 * @param pos
	 *            The position
	 * @return an array of cuboids found for the block at given location
	 */
	@Nullable
	default Cuboid6[] getCuboids(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		BlockTraceable block = Cast.cast(state.getBlock(), BlockTraceable.class);
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
	
	class Instance
	{
		public static IRayCubeGetter getter;
	}
}