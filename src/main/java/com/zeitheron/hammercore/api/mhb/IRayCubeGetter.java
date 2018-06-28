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
	public Cuboid6[] getBoundCubes6(BlockTraceable target);
	
	public ICubeManager getBoundCubeManager(BlockTraceable target);
	
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
	};
}