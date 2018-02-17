package com.pengu.hammercore.net;

import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public interface iPacketManager
{
	public String getChannel();
	
	public <PKT extends iPacket, REPLY extends iPacket> void registerPacket(Class<PKT> packet, iPacketListener<PKT, REPLY> listener);
	
	public <PKT extends iPacket> iPacketListener<PKT, ?> getListener(Class<PKT> packet);
	
	public void sendToAll(iPacket packet);
	
	public void sendTo(iPacket packet, EntityPlayerMP player);
	
	public void sendToAllAround(iPacket packet, TargetPoint point);
	
	public void sendToDimension(iPacket packet, int dimensionId);
	
	public void sendToServer(iPacket packet);
}