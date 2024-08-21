package org.zeith.hammerlib.api.tiles;

import net.minecraft.entity.player.EntityPlayer;

public interface IContainerTile
{
	default boolean hasGui()
	{
		return true;
	}
	
	Object getServerGuiElement(EntityPlayer player);
	
	Object getClientGuiElement(EntityPlayer player);
}