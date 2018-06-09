package com.zeitheron.hammercore.utils.charging;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

class VanillaPlayerInvLister implements IPlayerInventoryLister
{
	@Override
	public void listItemHandlers(EntityPlayer player, List<IItemHandlerModifiable> handlers)
	{
		handlers.add(new InvWrapper(player.inventory));
	}
}