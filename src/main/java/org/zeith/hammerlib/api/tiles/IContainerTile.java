package org.zeith.hammerlib.api.tiles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;

public interface IContainerTile
{
	default Container openContainer(PlayerEntity player, int windowId)
	{
		return null;
	}
}