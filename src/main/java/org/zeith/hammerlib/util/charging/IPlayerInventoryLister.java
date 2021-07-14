package org.zeith.hammerlib.util.charging;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

public interface IPlayerInventoryLister
{
	void listItemHandlers(PlayerEntity player, List<IItemHandlerModifiable> handlers);

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface InventoryLister
	{
	}
}