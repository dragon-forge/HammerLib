package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.MainThreaded;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;

/**
 * Syncs ANY tile entity to client.
 */
@MainThreaded
public class PacketSyncAnyTile
		implements IPacket
{
	private BlockPos pos;
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
		pos = tile.getPos().toImmutable();
		clazz = tile.getClass().getName();
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeCompoundTag(nbt);
		buf.writeBlockPos(pos);
	}
	
	@Override
	public void read(PacketBuffer buf)
			throws IOException
	{
		nbt = buf.readCompoundTag();
		pos = buf.readBlockPos();
	}
	
	@Override
	public void executeOnClient2(PacketContext net)
	{
		World world = net.getPlayer().world;
		if(world != null && world.isAreaLoaded(pos, pos) /* prevent crashing... */)
		{
			TileEntity tile = world.getTileEntity(pos);
			
			// try to recreate tile if we can
			// @since 1.5.3
			if(tile == null)
				try
				{
					tile = (TileEntity) Class.forName(clazz).newInstance();
				} catch(Throwable ignored)
				{
				}
			
			if(tile != null)
				tile.handleUpdateTag(nbt);
		}
	}
}