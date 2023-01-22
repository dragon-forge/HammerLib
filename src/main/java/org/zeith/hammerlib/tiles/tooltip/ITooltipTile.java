package org.zeith.hammerlib.tiles.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface ITooltipTile
{
	static ProgressBar[] NO_BARS = new ProgressBar[0];
	
	void getTextTooltip(List<Component> list, Player player);
	
	default boolean isEngineSupported(EnumTooltipEngine engine)
	{
		return true;
	}
	
	default boolean hasProgressBars(Player player)
	{
		ProgressBar[] bars = getProgressBars(player);
		return bars != null && bars.length > 0;
	}
	
	default ProgressBar[] getProgressBars(Player player)
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