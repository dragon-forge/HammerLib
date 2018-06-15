package com.zeitheron.hammercore.event.client;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Called on client side when a client is loaded up and world begins to render.
 * Call to start transferring custom packets to server.
 */
@SideOnly(Side.CLIENT)
public class ClientLoadedInEvent extends Event
{
}