package org.zeith.hammerlib.compat.curios;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.zeith.hammerlib.util.charging.IPlayerInventoryLister;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Consumer;

public class CuriosInvLister
		implements IPlayerInventoryLister
{
	@Override
	public void listItemHandlers(Player player, Consumer<IItemHandlerModifiable> handlers)
	{
		CuriosApi.getCuriosInventory(player)
				.ifPresent(h -> handlers.accept(h.getEquippedCurios()));
	}
}