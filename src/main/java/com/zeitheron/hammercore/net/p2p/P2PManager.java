package com.zeitheron.hammercore.net.p2p;

import java.util.UUID;

import com.zeitheron.hammercore.net.HCNet;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class P2PManager
{
	public P2PManager()
	{
	}
	
	@SideOnly(Side.CLIENT)
	public void sendTo(ITask packet, String... usernames)
	{
		HCNet.INSTANCE.sendToServer(new PacketSendTaskNamed(packet, usernames));
	}
	
	@SideOnly(Side.CLIENT)
	public void sendTo(ITask packet, UUID... playerUids)
	{
		HCNet.INSTANCE.sendToServer(new PacketSendTaskUUID(packet, playerUids));
	}
	
	@SideOnly(Side.CLIENT)
	public void sendToDimension(ITask packet, int... dims)
	{
		HCNet.INSTANCE.sendToServer(new PacketSendTaskDim(packet, dims));
	}
}