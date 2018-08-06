package com.zeitheron.hammercore.api.mhb;

import com.zeitheron.hammercore.utils.math.vec.Cuboid6;

/**
 * Registry class that allows modder to register hitboxes to a
 * {@link BlockTraceable}
 */
public interface IRayCubeRegistry
{
	/**
	 * Binds cuboids to {@link BlockTraceable}.
	 * 
	 * @param target
	 *            The target block
	 * @param boxes
	 *            The target cuboids
	 */
	public void bindBlockCube6(BlockTraceable target, Cuboid6... boxes);
	
	/**
	 * Binds cuboid manager to {@link BlockTraceable}.
	 * 
	 * @param target
	 *            The target block
	 * @param manager
	 *            The target manager
	 */
	public void bindBlockCubeManager(BlockTraceable target, ICubeManager manager);
}