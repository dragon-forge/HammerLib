package org.zeith.hammerlib.abstractions.sources;

import com.zeitheron.hammercore.utils.base.Cast;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.zeith.hammerlib.core.init.SourceTypesHL;


public class TileSourceType
		extends BaseObjectSourceEntry
{
	@Override
	public IObjectSource<TileEntity> readSource(NBTTagCompound tag)
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
		
		public TileSource(NBTTagCompound tag)
		{
			this.pos = new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
		}
		
		@Override
		public NBTTagCompound writeSource()
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("x", pos.getX());
			tag.setInteger("y", pos.getY());
			tag.setInteger("z", pos.getZ());
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
			return Cast.cast(world.getTileEntity(pos), TileEntity.class);
		}
		
		@Override
		public String toString()
		{
			return "TileSource{" +
					"x=" + pos.getX() +
					", y=" + pos.getY() +
					", z=" + pos.getZ() +
					'}';
		}
	}
}