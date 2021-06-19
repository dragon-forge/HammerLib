package com.zeitheron.hammercore.event;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Called on server side when a client is loaded up and world begins to render.
 * Call to start transferring custom packets to client.
 */
public class PlayerLoadReadyEvent
		extends PlayerEvent
{
	public final EntityPlayerMP playerMP;

	public PlayerLoadReadyEvent(EntityPlayerMP player)
	{
		super(player);
		this.playerMP = player;
	}
}