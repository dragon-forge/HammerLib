package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.MainThreaded;
import com.zeitheron.hammercore.net.PacketContext;
import com.zeitheron.hammercore.utils.StrPos;
import com.zeitheron.hammercore.utils.WorldUtil;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Syncs ANY tile entity to client.
 */
@MainThreaded
public class PacketSyncAnyTile implements IPacket
{
	private String pos;
	private int world;
	private NBTTagCompound nbt;
	
	private String clazz;
	
	static
	{
		IPacket.handle(PacketSyncAnyTile.class, PacketSyncAnyTile::new);
	}
	
	public PacketSyncAnyTile()
	{
	}
	
	public PacketSyncAnyTile(TileEntity tile)
	{
		nbt = tile.getUpdateTag();
		pos = StrPos.toStr(tile.getPos());
		world = tile.getWorld().provider.getDimension();
		clazz = tile.getClass().getName();
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("data", this.nbt);
		nbt.setString("pos", pos);
		nbt.setInteger("dim", world);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.nbt = nbt.getCompoundTag("data");
		pos = nbt.getString("pos");
		world = nbt.getInteger("dim");
	}
	
	@Override
	public IPacket execute(Side side, PacketContext net)
	{
		World world = WorldUtil.getWorld(net, this.world);
		BlockPos pos = StrPos.fromStr(this.pos);
		if(world != null && world.isAreaLoaded(pos, pos) /* prevent
		                                                  * crashing... */)
		{
			TileEntity tile = world.getTileEntity(pos);
			
			// try to recreate tile if we can
			// @since 1.5.3
			if(tile == null)
				try
				{
					tile = (TileEntity) Class.forName(clazz).newInstance();
				} catch(Throwable err)
				{
				}
			
			if(tile != null)
				tile.handleUpdateTag(nbt);
		}
		
		return null;
	}
}