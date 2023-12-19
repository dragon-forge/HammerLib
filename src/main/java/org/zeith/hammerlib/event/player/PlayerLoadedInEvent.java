package org.zeith.hammerlib.event.player;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class PlayerLoadedInEvent
		extends PlayerEvent
{
	public PlayerLoadedInEvent(ServerPlayer player)
	{
		super(player);
	}
	
	@Override
	public ServerPlayer getEntity()
	{
		return (ServerPlayer) super.getEntity();
	}
}