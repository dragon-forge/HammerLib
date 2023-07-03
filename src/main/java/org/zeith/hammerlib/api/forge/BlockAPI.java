package org.zeith.hammerlib.api.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import org.zeith.api.level.IBlockEntityLevel;
import org.zeith.hammerlib.api.tiles.ISidedTickableTile;
import org.zeith.hammerlib.mixins.BlockEntityAccessor;
import org.zeith.hammerlib.tiles.TileSyncableTickable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A class that provides utility methods for working with {@link BlockEntity} and {@link BlockEntityType}.
 */
public class BlockAPI
{
	/**
	 * Returns a {@link BlockEntityTicker} that ticks all {@link TileSyncableTickable} instances.
	 *
	 * @param <T>
	 * 		The type of {@link BlockEntity} that the ticker is for.
	 *
	 * @return A {@link BlockEntityTicker} that ticks {@link TileSyncableTickable} instances.
	 */
	public static <T extends BlockEntity> BlockEntityTicker<T> ticker()
	{
		return (level, pos, state, entity) ->
		{
			if(entity instanceof TileSyncableTickable t)
				t.tick(level, pos, state, entity);
		};
	}
	
	/**
	 * Returns a {@link BlockEntityTicker} that ticks all {@link ISidedTickableTile} instances using sided tickers to get rid of an extra side check.
	 *
	 * @param <T>
	 * 		The type of {@link BlockEntity} that the ticker is for.
	 *
	 * @return A {@link BlockEntityTicker} that ticks {@link TileSyncableTickable} instances.
	 */
	public static <T extends BlockEntity> BlockEntityTicker<T> ticker(Level lvl)
	{
		return lvl.isClientSide ? (level, pos, state, entity) ->
		{
			if(entity instanceof ISidedTickableTile t)
				t.clientTick(level, pos, state, entity);
		} : (level, pos, state, entity) ->
			   {
				   if(entity instanceof ISidedTickableTile t)
					   t.serverTick(level, pos, state, entity);
			   };
	}
	
	/**
	 * Creates a new {@link BlockEntityType} with the given generator and blocks.
	 * Used in conjunction with @{@link org.zeith.hammerlib.annotations.SimplyRegister} and @{@link org.zeith.hammerlib.annotations.RegistryName}
	 *
	 * @param <T>
	 * 		The type of {@link BlockEntity} that the type is for.
	 * @param generator
	 * 		The {@link BlockEntityType.BlockEntitySupplier} used to generate new instances of the {@link BlockEntity}.
	 * @param blocks
	 * 		The {@link Block}s that the {@link BlockEntityType} is associated with.
	 *
	 * @return A new {@link BlockEntityType} with the given generator and blocks.
	 */
	public static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityType.BlockEntitySupplier<T> generator, Block... blocks)
	{
		return BlockEntityType.Builder.of(generator, blocks).build(null);
	}
	
	/**
	 * Creates a new {@link BlockEntityType} with the given generator and blocks.
	 * Used in conjuction with @{@link org.zeith.hammerlib.annotations.SimplyRegister} and @{@link org.zeith.hammerlib.annotations.RegistryName}
	 *
	 * @param <T>
	 * 		The type of {@link BlockEntity} that the type is for.
	 * @param generator
	 * 		The {@link DynamicBlockEntitySupplier} used to generate new instances of the {@link BlockEntity}.
	 * @param blocks
	 * 		The {@link Block}s that the {@link BlockEntityType} is associated with.
	 *
	 * @return A new {@link BlockEntityType} with the given generator and blocks.
	 */
	public static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(DynamicBlockEntitySupplier<T> generator, Block... blocks)
	{
		AtomicReference<BlockEntityType<T>> typeRef = new AtomicReference<>();
		typeRef.set(BlockEntityType.Builder.of((pos, state) -> generator.create(typeRef.get(), pos, state), blocks)
				.build(null));
		return typeRef.get();
	}
	
	/**
	 * Returns a {@link List} of all loaded {@link BlockEntity} instances on the given {@link Level}.
	 *
	 * @param level
	 * 		The {@link Level} to get the {@link BlockEntity} instances from.
	 *
	 * @return A {@link List} of all loaded {@link BlockEntity} instances on the given {@link Level}.
	 */
	public static List<BlockEntity> getAllLoadedBlockEntities(Level level)
	{
		return IBlockEntityLevel.getLoadedBlockEntities(level);
	}
	
	/**
	 * Useful for cases when you want to extend a {@link BlockEntity} that has a fixed {@link BlockEntityType} as its parameter.
	 * Call inside a {@link BlockEntity} constructor(s) to ensure proper functionality.
	 *
	 * @param <T>
	 * 		The type of {@link BlockEntity} that the type is for.
	 * @param be
	 * 		The {@link BlockEntity} for which you want to spoof the {@link BlockEntityType}.
	 * @param type
	 * 		The {@link BlockEntityType} to spoof the {@link BlockEntity} as.
	 */
	public static <T extends BlockEntity> void spoofBlockEntityType(T be, BlockEntityType<T> type)
	{
		((BlockEntityAccessor) be).setType_HammerLib(type);
	}
	
	/**
	 * Sends a block update to the specified position in the given level. This method
	 * is used to trigger a visual update of a block and its surroundings in the client.
	 * It updates the block's model data, block state, and neighboring blocks.
	 *
	 * @param level
	 * 		The level in which the block update should occur.
	 * @param pos
	 * 		The position of the block that needs to be updated.
	 */
	public static void sendBlockUpdate(Level level, BlockPos pos)
	{
		if(level == null || pos == null) return;
		Optional.ofNullable(level.getBlockEntity(pos)).ifPresent(BlockEntity::requestModelDataUpdate);
		var state = level.getBlockState(pos);
		level.sendBlockUpdated(pos, state, state, 3);
		level.blockUpdated(pos, state.getBlock());
		level.setBlockAndUpdate(pos, Block.updateFromNeighbourShapes(state, level, pos));
	}
	
	@FunctionalInterface
	public interface DynamicBlockEntitySupplier<T extends BlockEntity>
	{
		T create(BlockEntityType<T> type, BlockPos pos, BlockState state);
	}
}