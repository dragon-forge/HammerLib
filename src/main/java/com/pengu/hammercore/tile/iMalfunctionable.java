package com.pengu.hammercore.tile;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

/**
 * Used to make {@link TileEntity} malfunction for some time. Used by Lost
 * Thaumaturgy, atm.
 */
public interface iMalfunctionable
{
	void causeGeneralMalfunction();
	
	default void causeEntityMalfunction(Entity entity)
	{
		causeGeneralMalfunction();
	};
}