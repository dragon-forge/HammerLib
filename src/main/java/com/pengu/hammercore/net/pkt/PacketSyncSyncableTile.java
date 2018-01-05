package com.pengu.hammercore.net.pkt;

import com.pengu.hammercore.common.utils.StrPos;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;
import com.pengu.hammercore.tile.TileSyncable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncSyncableTile implements iPacket, iPacketListener<PacketSyncSyncableTile, iPacket>
{
	private String pos;
	private int world;
	private NBTTagCompound nbt;
	
	private String clazz;
	
	public PacketSyncSyncableTile()
	{
	}
	
	public PacketSyncSyncableTile(TileSyncable tile)
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
	public iPacket onArrived(PacketSyncSyncableTile packet, MessageContext context)
	{
		World world = WorldUtil.getWorld(context, packet.world);
		BlockPos pos = StrPos.fromStr(packet.pos);
		if(world != null && world.isAreaLoaded(pos, pos) /* prevent
		                                                  * crashing... */)
		{
			TileSyncable sync = WorldUtil.cast(world.getTileEntity(pos), TileSyncable.class);
			
			// try to recreate tile if we can
			// @since 1.5.3
			if(sync == null)
				try
				{
					sync = (TileSyncable) Class.forName(packet.clazz).newInstance();
				} catch(Throwable err)
				{
				}
			
			if(sync != null)
				sync.handleUpdateTag(packet.nbt);
		}
		
		return null;
	}
	
}