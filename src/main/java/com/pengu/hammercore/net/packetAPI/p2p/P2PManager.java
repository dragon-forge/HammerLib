package com.pengu.hammercore.net.packetAPI.p2p;

import java.util.UUID;

import com.pengu.hammercore.net.packetAPI.PacketManager;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class P2PManager
{
	public final PacketManager mgr;
	
	public P2PManager(PacketManager mgr)
	{
		this.mgr = mgr;
	}
	
	@SideOnly(Side.CLIENT)
	public void sendTo(iTask packet, String... usernames)
	{
		mgr.sendToServer(new PacketSendTaskNamed(packet, usernames));
	}
	
	@SideOnly(Side.CLIENT)
	public void sendTo(iTask packet, UUID... playerUids)
	{
		mgr.sendToServer(new PacketSendTaskUUID(packet, playerUids));
	}
	
	@SideOnly(Side.CLIENT)
	public void sendToDimension(iTask packet, int... dims)
	{
		mgr.sendToServer(new PacketSendTaskDim(packet, dims));
	}
}