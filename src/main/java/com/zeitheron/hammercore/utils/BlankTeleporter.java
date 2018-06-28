package com.zeitheron.hammercore.utils;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class BlankTeleporter extends Teleporter
{
	public BlankTeleporter(WorldServer worldIn)
	{
		super(worldIn);
	}
	
	@Override
	public void placeInPortal(Entity entityIn, float rotationYaw)
	{
		entityIn.motionX = 0;
		entityIn.motionY = 0;
		entityIn.motionZ = 0;
	}
	
	@Override
	public boolean makePortal(Entity entityIn)
	{
		return true;
	}
}