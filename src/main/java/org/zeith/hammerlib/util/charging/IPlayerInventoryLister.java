package org.zeith.hammerlib.util.charging;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
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