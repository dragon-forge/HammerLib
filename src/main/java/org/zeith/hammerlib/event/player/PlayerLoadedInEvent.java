package org.zeith.hammerlib.event.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerLoadedInEvent
		extends PlayerEvent
{
	public PlayerLoadedInEvent(ServerPlayer player)
	{
		super(player);
	}

	@Override
	public ServerPlayer getPlayer()
	{
		return (ServerPlayer) super.getPlayer();
	}
}