package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.event.PlayerLoadReadyEvent;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraftforge.common.MinecraftForge;

public class PacketPlayerReady implements IPacket
{
	static
	{
		IPacket.handle(PacketPlayerReady.class, () -> new PacketPlayerReady());
	}
	
	@Override
	public IPacket executeOnServer(PacketContext net)
	{
		MinecraftForge.EVENT_BUS.post(new PlayerLoadReadyEvent(net.getSender()));
		return null;
	}
}