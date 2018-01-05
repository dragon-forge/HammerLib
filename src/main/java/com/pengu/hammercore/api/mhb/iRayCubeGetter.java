package com.pengu.hammercore.api.mhb;

import com.pengu.hammercore.vec.Cuboid6;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Registry class that allows modder to get hitboxes for a
 * {@link BlockTraceable}
 */
public interface iRayCubeGetter
{
	public Cuboid6[] getBoundCubes6(BlockTraceable target);
	
	public iCubeManager getBoundCubeManager(BlockTraceable target);
	
	public EntityPlayer func_0x834823_a();
	
	public static class Instance
	{
		public static iRayCubeGetter getter;
	};
}