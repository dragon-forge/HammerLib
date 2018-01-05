package com.pengu.hammercore.api;

import net.minecraft.tileentity.TileEntity;

public interface iTileBlock<T extends TileEntity>
{
	public Class<T> getTileClass();
	
	default T newTile()
	{
		try
		{
			return getTileClass().getDeclaredConstructor().newInstance();
		} catch(Throwable err)
		{
			err.printStackTrace();
		}
		
		return null;
	}
}