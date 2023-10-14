package com.zeitheron.hammercore;

import com.zeitheron.hammercore.api.mhb.*;
import com.zeitheron.hammercore.utils.base.Cast;
import com.zeitheron.hammercore.utils.math.vec.Cuboid6;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public final class RayCubeRegistry
		implements IRayCubeRegistry, IRayCubeGetter
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

	public static Cuboid6[] getCuboidsAt(World world, BlockPos pos)
	{
		BlockTraceable bt = Cast.cast(world.getBlockState(pos).getBlock(), BlockTraceable.class);
		if(bt != null)
		{
			ICubeManager mgr = RayCubeRegistry.instance.getBoundCubeManager(bt);
			if(mgr != null)
				return mgr.getCuboids(world, pos, world.getBlockState(pos));
			Cuboid6[] cbs = RayCubeRegistry.instance.getBoundCubes6(bt);
			if(cbs != null)
				return cbs;
		}
		return new Cuboid6[]{ new Cuboid6(world.getBlockState(pos).getBoundingBox(world, pos)) };
	}
}