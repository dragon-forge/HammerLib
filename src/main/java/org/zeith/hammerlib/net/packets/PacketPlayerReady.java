package org.zeith.hammerlib.net.packets;

import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.player.PlayerLoadedInEvent;
import org.zeith.hammerlib.net.*;

@MainThreaded
public class PacketPlayerReady
		implements IPacket
{
	@Override
	public void serverExecute(PacketContext ctx)
	{
		HammerLib.postNeoEvent(new PlayerLoadedInEvent(ctx.getSender()));
	}
}