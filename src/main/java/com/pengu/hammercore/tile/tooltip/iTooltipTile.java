package com.pengu.hammercore.tile.tooltip;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface iTooltipTile
{
	static ProgressBar[] NO_BARS = new ProgressBar[0];
	
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
		return NO_BARS;
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