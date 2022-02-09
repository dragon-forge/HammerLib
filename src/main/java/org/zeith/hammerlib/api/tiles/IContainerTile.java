package org.zeith.hammerlib.api.tiles;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import javax.annotation.Nullable;

public interface IContainerTile
{
	default AbstractContainerMenu openContainer(Player player, int windowId)
	{
		return null;
	}

	@Nullable
	default Component getDisplayName()
	{
		return null;
	}
}