package org.zeith.hammerlib.util.charging;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import java.lang.annotation.*;
import java.util.function.Consumer;

public interface IPlayerInventoryLister
{
	void listItemHandlers(Player player, Consumer<IItemHandlerModifiable> handlers);

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface InventoryLister
	{
	}
}