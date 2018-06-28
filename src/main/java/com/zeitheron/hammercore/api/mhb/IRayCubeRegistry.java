package com.zeitheron.hammercore.api.mhb;

import com.zeitheron.hammercore.utils.math.vec.Cuboid6;

/**
 * Registry class that allows modder to register hitboxes to a
 * {@link BlockTraceable}
 */
public interface IRayCubeRegistry
{
	public void bindBlockCube6(BlockTraceable target, Cuboid6... boxes);
	
	public void bindBlockCubeManager(BlockTraceable target, ICubeManager manager);
}