package com.pengu.hammercore.net.pkt;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketReloadRaytracePlugins implements iPacket, iPacketListener<PacketReloadRaytracePlugins, iPacket>
{
	
	@Override
	public iPacket onArrived(PacketReloadRaytracePlugins packet, MessageContext context)
	{
		HammerCore.instance.reloadRaytracePlugins();
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
	}
}