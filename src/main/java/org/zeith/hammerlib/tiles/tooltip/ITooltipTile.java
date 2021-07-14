package org.zeith.hammerlib.tiles.tooltip;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface ITooltipTile
{
	static ProgressBar[] NO_BARS = new ProgressBar[0];

	void getTextTooltip(List<String> list, PlayerEntity player);

	default boolean isEngineSupported(EnumTooltipEngine engine)
	{
		return true;
	}

	default boolean hasProgressBars(PlayerEntity player)
	{
		ProgressBar[] bars = getProgressBars(player);
		return bars != null && bars.length > 0;
	}

	default ProgressBar[] getProgressBars(PlayerEntity player)
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