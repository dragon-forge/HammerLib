package com.pengu.hammercore.net.pkt;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;
import com.pengu.hammercore.net.utils.NetPropertyAbstract;
import com.pengu.hammercore.tile.TileSyncable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSetProperty implements iPacket, iPacketListener<PacketSetProperty, iPacket>
{
	protected NBTTagCompound nbt;
	
	@SideOnly(Side.CLIENT)
	public static void toServer(TileSyncable tile, NetPropertyAbstract abs)
	{
		HCNetwork.manager.sendToServer(new PacketSetProperty(tile, abs));
	}
	
	public PacketSetProperty(TileSyncable tile, NetPropertyAbstract property)
	{
		nbt = new NBTTagCompound();
		nbt.setLong("Pos", tile.getPos().toLong());
		nbt.setInteger("Dim", tile.getWorld().provider.getDimension());
		nbt.setTag("Data", property.writeToNBT(new NBTTagCompound()));
		nbt.setInteger("Id", property.getId());
	}
	
	public PacketSetProperty()
	{
	}
	
	@Override
	public iPacket onArrived(PacketSetProperty packet, MessageContext context)
	{
		if(context.side == Side.SERVER)
		{
			NBTTagCompound nbt = packet.nbt;
			
			int dim = nbt.getInteger("Dim");
			BlockPos pos = BlockPos.fromLong(nbt.getLong("Pos"));
			NBTTagCompound prop = nbt.getCompoundTag("Data");
			int id = nbt.getInteger("Id");
			
			MinecraftServer server = context.getServerHandler().player.mcServer;
			WorldServer world = server.getWorld(dim);
			
			if(world != null && world.isBlockLoaded(pos) && world.getTileEntity(pos) instanceof TileSyncable)
				((TileSyncable) world.getTileEntity(pos)).load(id, prop);
		} else
			HammerCore.LOG.warn("Attempted to run PacketSetProperty on client. This is not going to work! Use TileSyncable.sync() instead!");
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("Data", this.nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.nbt = nbt.getCompoundTag("Data");
	}
}