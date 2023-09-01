package org.zeith.hammerlib.abstractions.sources;

import lombok.var;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.zeith.hammerlib.core.init.SourceTypesHL;
import org.zeith.hammerlib.util.java.Cast;

public class TileSourceType
		extends IObjectSourceType
{
	@Override
	public IObjectSource<TileEntity> readSource(CompoundNBT tag)
	{
		return new TileSource(tag);
	}
	
	public static class TileSource
			implements IObjectSource<TileEntity>
	{
		public final BlockPos pos;
		
		public TileSource(BlockPos pos)
		{
			this.pos = pos;
		}
		
		public TileSource(CompoundNBT tag)
		{
			this.pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
		}
		
		@Override
		public CompoundNBT writeSource()
		{
			var tag = new CompoundNBT();
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
		public Class<TileEntity> getBaseType()
		{
			return TileEntity.class;
		}
		
		@Override
		public TileEntity get(World world)
		{
			return Cast.cast(world.getBlockEntity(pos), TileEntity.class);
		}
	}
}