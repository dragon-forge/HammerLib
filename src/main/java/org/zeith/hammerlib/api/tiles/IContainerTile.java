package org.zeith.hammerlib.api.tiles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public interface IContainerTile
{
	default Container openContainer(PlayerEntity player, int windowId)
	{
		return null;
	}

	@Nullable
	default ITextComponent getDisplayName()
	{
		return null;
	}
}