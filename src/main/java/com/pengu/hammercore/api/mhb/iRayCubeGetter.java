package com.pengu.hammercore.api.mhb;

import javax.annotation.Nullable;

import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.vec.Cuboid6;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Registry class that allows modder to get hitboxes for a
 * {@link BlockTraceable}
 */
public interface iRayCubeGetter
{
	public Cuboid6[] getBoundCubes6(BlockTraceable target);
	
	public iCubeManager getBoundCubeManager(BlockTraceable target);
	
	@Nullable
	default Cuboid6[] getCuboids(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		BlockTraceable block = WorldUtil.cast(state.getBlock(), BlockTraceable.class);
		if(block != null)
		{
			iCubeManager mgr = getBoundCubeManager(block);
			if(mgr != null)
				return mgr.getCuboids(world, pos, state);
			else
				return getBoundCubes6(block);
		}
		return null;
	}
	
	public static class Instance
	{
		public static iRayCubeGetter getter;
	};
}