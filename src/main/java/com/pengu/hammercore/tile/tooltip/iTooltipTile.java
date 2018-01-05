package com.pengu.hammercore.tile.tooltip;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface iTooltipTile
{
	void getTextTooltip(List<String> list, EntityPlayer player);
	
	default boolean isEngineSupported(eTooltipEngine engine)
	{
		return true;
	}
	
	default boolean hasProgressBars(EntityPlayer player)
	{
		ProgressBar[] bars = getProgressBars(player);
		return bars != null && bars.length > 0;
	}
	
	default ProgressBar[] getProgressBars(EntityPlayer player)
	{
		return null;
	}
	
	default boolean hasItemIconOverride()
	{
		return false;
	}
	
	default ItemStack getItemIconOverride()
	{
		return ItemStack.EMPTY;
	}
}