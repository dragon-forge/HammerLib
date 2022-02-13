package org.zeith.api.level;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collections;
import java.util.List;

public interface IBlockEntityLevel
{
	static List<BlockEntity> getLoadedBlockEntities(Level lvl)
	{
		if(lvl instanceof IBlockEntityLevel)
			return ((IBlockEntityLevel) lvl).getLoadedBlockEntities_HammerLib();
		return Collections.emptyList();
	}

	static void loadBlockEntity(Level lvl, BlockEntity be)
	{
		if(lvl instanceof IBlockEntityLevel)
			((IBlockEntityLevel) lvl).loadBlockEntity_HammerLib(be);
	}

	static void unloadBlockEntity(Level lvl, BlockEntity be)
	{
		if(lvl instanceof IBlockEntityLevel)
			((IBlockEntityLevel) lvl).unloadBlockEntity_HammerLib(be);
	}

	void loadBlockEntity_HammerLib(BlockEntity be);

	void unloadBlockEntity_HammerLib(BlockEntity be);

	List<BlockEntity> getLoadedBlockEntities_HammerLib();
}