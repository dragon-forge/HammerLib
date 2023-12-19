package org.zeith.hammerlib.abstractions.sources;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.zeith.hammerlib.core.init.SourceTypesHL;
import org.zeith.hammerlib.util.java.Cast;


public class TileSourceType
		implements IObjectSourceType
{
	@Override
	public IObjectSource<BlockEntity> readSource(CompoundTag tag)
	{
		return new TileSource(tag);
	}
	
	public static class TileSource
			implements IObjectSource<BlockEntity>
	{
		public final BlockPos pos;
		
		public TileSource(BlockPos pos)
		{
			this.pos = pos;
		}
		
		public TileSource(CompoundTag tag)
		{
			this.pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
		}
		
		@Override
		public CompoundTag writeSource()
		{
			var tag = new CompoundTag();
			tag.putInt("x", pos.getX());
			tag.putInt("y", pos.getY());
			tag.putInt("z", pos.getZ());
			return tag;
		}
		
		@Override
		public IObjectSourceType getType()
		{
			return SourceTypesHL.TILE_TYPE;
		}
		
		@Override
		public Class<BlockEntity> getBaseType()
		{
			return BlockEntity.class;
		}
		
		@Override
		public BlockEntity get(Level world)
		{
			return Cast.cast(world.getBlockEntity(pos), BlockEntity.class);
		}
	}
}