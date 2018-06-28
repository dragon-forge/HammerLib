package com.zeitheron.hammercore;

import java.util.HashMap;
import java.util.Map;

import com.zeitheron.hammercore.api.mhb.BlockTraceable;
import com.zeitheron.hammercore.api.mhb.ICubeManager;
import com.zeitheron.hammercore.api.mhb.IRayCubeGetter;
import com.zeitheron.hammercore.api.mhb.IRayCubeRegistry;
import com.zeitheron.hammercore.utils.math.vec.Cuboid6;

import net.minecraft.block.Block;

public final class RayCubeRegistry implements IRayCubeRegistry, IRayCubeGetter
{
	public static final RayCubeRegistry instance = new RayCubeRegistry();
	final Map<Block, Cuboid6[]> cubes = new HashMap<Block, Cuboid6[]>();
	final Map<Block, ICubeManager> mgrs = new HashMap<Block, ICubeManager>();
	
	private RayCubeRegistry()
	{
		Instance.getter = this;
	}
	
	@Override
	public void bindBlockCube6(BlockTraceable target, Cuboid6... boxes)
	{
		cubes.put(target, boxes);
	}
	
	@Override
	public Cuboid6[] getBoundCubes6(BlockTraceable target)
	{
		return mgrs.get(target) == null ? cubes.get(target) != null ? cubes.get(target) : new Cuboid6[0] : null;
	}
	
	@Override
	public void bindBlockCubeManager(BlockTraceable target, ICubeManager manager)
	{
		mgrs.put(target, manager);
	}
	
	@Override
	public ICubeManager getBoundCubeManager(BlockTraceable target)
	{
		return mgrs.get(target);
	}
}