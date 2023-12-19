package org.zeith.hammerlib.net.packets;

import net.neoforged.neoforge.common.NeoForge;
import org.zeith.hammerlib.event.player.PlayerLoadedInEvent;
import org.zeith.hammerlib.net.*;

@MainThreaded
public class PacketPlayerReady
		implements IPacket
{
	@Override
	public void serverExecute(PacketContext ctx)
	{
		NeoForge.EVENT_BUS.post(new PlayerLoadedInEvent(ctx.getSender()));
	}
}