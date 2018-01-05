package com.pengu.hammercore.net.pkt.script;

import com.pengu.hammercore.HammerCore.GRCProvider;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketReloadScripts implements iPacket, iPacketListener<PacketReloadScripts, iPacket>
{
	
	@Override
	public iPacket onArrived(PacketReloadScripts packet, MessageContext context)
	{
		GRCProvider.reloadScript();
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