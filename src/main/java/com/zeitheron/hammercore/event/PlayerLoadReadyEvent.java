package com.zeitheron.hammercore.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Called on server side when a client is loaded up and world begins to render.
 * Call to start transferring custom packets to client.
 */
public class PlayerLoadReadyEvent extends PlayerEvent
{
	public PlayerLoadReadyEvent(EntityPlayer player)
	{
		super(player);
	}
}