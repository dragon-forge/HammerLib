package com.pengu.hammercore;

import java.util.HashMap;
import java.util.Map;

import com.pengu.hammercore.api.mhb.BlockTraceable;
import com.pengu.hammercore.api.mhb.iCubeManager;
import com.pengu.hammercore.api.mhb.iRayCubeGetter;
import com.pengu.hammercore.api.mhb.iRayCubeRegistry;
import com.pengu.hammercore.vec.Cuboid6;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;

final class RayCubeRegistry implements iRayCubeRegistry, iRayCubeGetter
{
	static final RayCubeRegistry instance = new RayCubeRegistry();
	final Map<Block, Cuboid6[]> cubes = new HashMap<Block, Cuboid6[]>();
	final Map<Block, iCubeManager> mgrs = new HashMap<Block, iCubeManager>();
	
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
	public void bindBlockCubeManager(BlockTraceable target, iCubeManager manager)
	{
		mgrs.put(target, manager);
	}
	
	@Override
	public iCubeManager getBoundCubeManager(BlockTraceable target)
	{
		return mgrs.get(target);
	}
	
	@Override
	public EntityPlayer func_0x834823_a()
	{
		return HammerCore.renderProxy.getClientPlayer();
	}
}