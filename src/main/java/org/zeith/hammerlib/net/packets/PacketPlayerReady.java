package org.zeith.hammerlib.net.packets;

import net.minecraftforge.common.MinecraftForge;
import org.zeith.hammerlib.event.player.PlayerLoadedInEvent;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketPlayerReady
		implements IPacket
{
	@Override
	public void serverExecute(PacketContext ctx)
	{
		MinecraftForge.EVENT_BUS.post(new PlayerLoadedInEvent(ctx.getSender()));
	}
}