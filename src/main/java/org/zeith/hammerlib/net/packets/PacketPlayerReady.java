package org.zeith.hammerlib.net.packets;

import net.minecraftforge.common.MinecraftForge;
import org.zeith.hammerlib.api.crafting.AbstractRecipeRegistry;
import org.zeith.hammerlib.event.player.PlayerLoadedInEvent;
import org.zeith.hammerlib.net.*;

@MainThreaded
public class PacketPlayerReady
		implements IPacket
{
	@Override
	public void serverExecute(PacketContext ctx)
	{
		for(var reg : AbstractRecipeRegistry.getAllRegistries())
		{
			reg.syncToPlayer(ctx.getSender());
		}
		
		MinecraftForge.EVENT_BUS.post(new PlayerLoadedInEvent(ctx.getSender()));
	}
}