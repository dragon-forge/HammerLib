package org.zeith.hammerlib.api.forge;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.zeith.api.level.IBlockEntityLevel;
import org.zeith.hammerlib.tiles.TileSyncableTickable;

import java.util.List;

public class BlockAPI
{
	public static <T extends BlockEntity> BlockEntityTicker<T> ticker()
	{
		return (level, pos, state, entity) ->
		{
			if(entity instanceof TileSyncableTickable t)
				t.tick(level, pos, state, entity);
		};
	}

	public static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityType.BlockEntitySupplier<T> generator, Block... blocks)
	{
		return BlockEntityType.Builder.of(generator, blocks).build(null);
	}

	public static List<BlockEntity> getAllLoadedBlockEntities(Level level)
	{
		return IBlockEntityLevel.getLoadedBlockEntities(level);
	}
}