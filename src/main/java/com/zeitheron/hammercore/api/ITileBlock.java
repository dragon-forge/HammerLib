package com.zeitheron.hammercore.api;

import net.minecraft.tileentity.TileEntity;

public interface ITileBlock<T extends TileEntity>
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