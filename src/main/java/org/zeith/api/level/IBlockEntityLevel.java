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
			return ((IBlockEntityLevel) lvl).getLoadedBlockEntities_HL();
		return Collections.emptyList();
	}

	static void loadBlockEntity(Level lvl, BlockEntity be)
	{
		if(lvl instanceof IBlockEntityLevel)
			((IBlockEntityLevel) lvl).loadBlockEntity_HL(be);
	}

	static void unloadBlockEntity(Level lvl, BlockEntity be)
	{
		if(lvl instanceof IBlockEntityLevel)
			((IBlockEntityLevel) lvl).unloadBlockEntity_HL(be);
	}

	void loadBlockEntity_HL(BlockEntity be);

	void unloadBlockEntity_HL(BlockEntity be);

	List<BlockEntity> getLoadedBlockEntities_HL();
}