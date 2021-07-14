package org.zeith.hammerlib.util.charging.impl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.zeith.hammerlib.util.charging.IPlayerInventoryLister;

import java.util.List;

@IPlayerInventoryLister.InventoryLister
class VanillaPlayerInvLister
		implements IPlayerInventoryLister
{
	@Override
	public void listItemHandlers(PlayerEntity player, List<IItemHandlerModifiable> handlers)
	{
		handlers.add(new InvWrapper(player.inventory));
	}
}