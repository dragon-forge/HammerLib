package org.zeith.hammerlib.core.adapter;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.mixins.BlockEntityTypeAccessor;
import org.zeith.hammerlib.util.java.Cast;

import java.util.*;

public class BlockEntityAdapter
{
	public static Set<Block> getValidBlocks(BlockEntityType<?> type)
	{
		return type.getValidBlocks();
	}
	
	public static synchronized void addBlocksToEntityType(BlockEntityType<?> type, Block... blocks)
	{
		BlockEntityTypeAccessor ac = Cast.cast(type, BlockEntityTypeAccessor.class);
		
		if(ac == null)
			throw new UnsupportedOperationException("The BlockEntityType mixin accessor failed to apply...");
		
		var valid = getValidBlocks(type);
		if(!tryAdd(valid, blocks))
		{
			valid = new HashSet<>(valid);
			valid.addAll(List.of(blocks));
			ac.neoforge$setValidBlocks(valid);
		}
	}
	
	private static synchronized <T> boolean tryAdd(Collection<T> coll, T... all)
	{
		try
		{
			return coll.addAll(List.of(all));
		} catch(UnsupportedOperationException e)
		{
			return false;
		}
	}
}