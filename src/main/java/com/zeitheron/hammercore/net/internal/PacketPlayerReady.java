package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.event.PlayerLoadReadyEvent;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.MainThreaded;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraftforge.common.MinecraftForge;

@MainThreaded
public class PacketPlayerReady implements IPacket
{
	static
	{
		IPacket.handle(PacketPlayerReady.class, PacketPlayerReady::new);
	}
	
	@Override
	public void executeOnServer2(PacketContext net)
	{
		MinecraftForge.EVENT_BUS.post(new PlayerLoadReadyEvent(net.getSender()));
	}
}