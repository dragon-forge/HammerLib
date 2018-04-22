package com.pengu.hammercore.net.pkt.opts;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.client.HCClientOptions;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;
import com.pengu.hammercore.proxy.RenderProxy_Client;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketReqOpts implements iPacket, iPacketListener<PacketReqOpts, PacketCHCOpts>
{
	@Override
	public PacketCHCOpts onArrived(PacketReqOpts packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			RenderProxy_Client.needsClConfigSync = true;
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