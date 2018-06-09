package com.zeitheron.hammercore.utils.charging.modules.baubles;

import java.util.List;

import com.zeitheron.hammercore.utils.charging.IPlayerInventoryLister;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.IItemHandlerModifiable;

class BaubleInvLister implements IPlayerInventoryLister
{
	@Override
	public void listItemHandlers(EntityPlayer player, List<IItemHandlerModifiable> handlers)
	{
		handlers.add(BaublesApi.getBaublesHandler(player));
	}
}