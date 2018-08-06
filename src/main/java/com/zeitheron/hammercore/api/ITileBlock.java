package com.zeitheron.hammercore.api;

import com.zeitheron.hammercore.internal.SimpleRegistration;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Allows {@link SimpleRegistration} to register a {@link TileEntity} class that
 * this block contains, automatically. This interface also extends
 * {@link ITileEntityProvider} which means that this block MUST have a
 * {@link TileEntity}. Note: only applies when block is registered through
 * {@link SimpleRegistration} class.
 */
public interface ITileBlock<T extends TileEntity> extends ITileEntityProvider
{
	/**
	 * Gets the class of {@link TileEntity} that will be registered.
	 */
	public Class<T> getTileClass();
	
	@Override
	default TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return newTile();
	}
	
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