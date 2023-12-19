package org.zeith.hammerlib.util.charging.impl;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.zeith.hammerlib.util.charging.IPlayerInventoryLister;

import java.util.function.Consumer;

@IPlayerInventoryLister.InventoryLister
class VanillaPlayerInvLister
		implements IPlayerInventoryLister
{
	@Override
	public void listItemHandlers(Player player, Consumer<IItemHandlerModifiable> handlers)
	{
		handlers.accept(new InvWrapper(player.getInventory()));
	}
}