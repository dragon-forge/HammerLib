package org.zeith.api.level;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collections;
import java.util.List;

/**
 * Interface for adding additional functionality to the Level class for handling block entities.
 * Provides methods for getting a list of loaded block entities, loading a block entity, and unloading a block entity.
 */
public interface IBlockEntityLevel
{
	/**
	 * Returns a list of loaded block entities for the provided Level.
	 *
	 * @param lvl
	 * 		Level to get the list of loaded block entities for
	 *
	 * @return list of loaded block entities for the provided Level
	 */
	static List<BlockEntity> getLoadedBlockEntities(Level lvl)
	{
		if(lvl instanceof IBlockEntityLevel)
			return ((IBlockEntityLevel) lvl).getLoadedBlockEntities_HammerLib();
		return Collections.emptyList();
	}
	
	/**
	 * Loads the provided block entity into the provided Level.
	 *
	 * @param lvl
	 * 		Level to load the block entity into
	 * @param be
	 * 		block entity to be loaded
	 */
	static void loadBlockEntity(Level lvl, BlockEntity be)
	{
		if(lvl instanceof IBlockEntityLevel)
			((IBlockEntityLevel) lvl).loadBlockEntity_HammerLib(be);
	}
	
	/**
	 * Unloads the provided block entity from the provided Level.
	 *
	 * @param lvl
	 * 		Level to unload the block entity from
	 * @param be
	 * 		block entity to be unloaded
	 */
	static void unloadBlockEntity(Level lvl, BlockEntity be)
	{
		if(lvl instanceof IBlockEntityLevel)
			((IBlockEntityLevel) lvl).unloadBlockEntity_HammerLib(be);
	}
	
	/**
	 * Loads the provided block entity into the Level implementing this interface.
	 *
	 * @param be
	 * 		block entity to be loaded
	 *
	 * @note For internal use only. Do not call directly.
	 */
	void loadBlockEntity_HammerLib(BlockEntity be);
	
	/**
	 * Unloads the provided block entity from the Level implementing this interface.
	 *
	 * @param be
	 * 		block entity to be unloaded
	 *
	 * @note For internal use only. Do not call directly.
	 */
	void unloadBlockEntity_HammerLib(BlockEntity be);
	
	/**
	 * Returns a list of loaded block entities for the Level implementing this interface.
	 *
	 * @return list of loaded block entities for the Level implementing this interface
	 *
	 * @note For internal use only. Do not call directly.
	 */
	List<BlockEntity> getLoadedBlockEntities_HammerLib();
}