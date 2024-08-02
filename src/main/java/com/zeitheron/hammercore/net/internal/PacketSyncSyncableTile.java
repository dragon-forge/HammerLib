package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.net.*;
import com.zeitheron.hammercore.tile.TileSyncable;
import com.zeitheron.hammercore.utils.base.Cast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;

@MainThreaded
public class PacketSyncSyncableTile
		implements IPacket
{
	private BlockPos pos;
	private NBTTagCompound nbt;
	
	private String clazz;
	
	static
	{
		IPacket.handle(PacketSyncSyncableTile.class, PacketSyncSyncableTile::new);
	}
	
	public PacketSyncSyncableTile()
	{
	}
	
	public PacketSyncSyncableTile(TileSyncable tile)
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
			TileSyncable sync = Cast.cast(world.getTileEntity(pos), TileSyncable.class);
			
			// try to recreate tile if we can
			// @since 1.5.3
			if(sync == null)
				try
				{
					sync = (TileSyncable) Class.forName(clazz).newInstance();
				} catch(Throwable err)
				{
				}
			
			if(sync != null)
			{
				sync.onPreSync(nbt);
				sync.handleUpdateTag(nbt);
				sync.onSynced();
			}
		}
	}
	
	@Override
	public void executeOnServer2(PacketContext net)
	{
		EntityPlayer player = net.getPlayer();
		if(player == null) return;
		TileEntity te = player.world.getTileEntity(pos);
		if(te instanceof TileSyncable)
			net.withReply(new PacketSyncSyncableTile((TileSyncable) te));
	}
}