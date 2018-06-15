package com.zeitheron.hammercore.netv2.internal;

import com.zeitheron.hammercore.event.PlayerLoadReadyEvent;
import com.zeitheron.hammercore.netv2.IV2Packet;
import com.zeitheron.hammercore.netv2.PacketContext;

import net.minecraftforge.common.MinecraftForge;

public class PacketPlayerReady implements IV2Packet
{
	static
	{
		IV2Packet.handle(PacketPlayerReady.class, () -> new PacketPlayerReady());
	}
	
	@Override
	public IV2Packet executeOnServer(PacketContext net)
	{
		MinecraftForge.EVENT_BUS.post(new PlayerLoadReadyEvent(net.getSender()));
		return null;
	}
}