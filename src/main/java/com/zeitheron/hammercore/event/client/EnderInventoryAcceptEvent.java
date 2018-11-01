package com.zeitheron.hammercore.event.client;

import com.zeitheron.hammercore.net.internal.PacketSyncEnderChest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Called from {@link PacketSyncEnderChest#executeOnClient(com.zeitheron.hammercore.net.PacketContext)}
 */
@SideOnly(Side.CLIENT)
public class EnderInventoryAcceptEvent extends PlayerEvent
{
	public EnderInventoryAcceptEvent(EntityPlayer player)
	{
		super(player);
	}
}