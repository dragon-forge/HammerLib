package com.zeitheron.hammercore.utils.charging;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IPlayerInventoryLister
{
	void listItemHandlers(EntityPlayer player, List<IItemHandlerModifiable> handlers);
}