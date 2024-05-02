package org.zeith.hammerlib.tiles.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.proxy.HLConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * The abstract interface to be applied onto {@link net.minecraft.world.level.block.entity.BlockEntity}.
 * <p>
 * It enables tooltip engines like Jade or The One Probe to fill tooltip using {@link #addTooltip(ITooltipConsumer, Player)} method.
 */
public interface ITooltipTile
{
	ProgressBar[] NO_BARS = new ProgressBar[0];
	
	default void getTextTooltip(List<Component> list, Player player) {}
	
	default boolean isEngineSupported(EnumTooltipEngine engine)
	{
		return HLConstants.enableHammerLibTooltipEngine || engine != EnumTooltipEngine.HAMMER_LIB;
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
	
	default void addTooltip(ITooltipConsumer consumer, Player player)
	{
		List<Component> tip = new ArrayList<>();
		getTextTooltip(tip, player);
		for(var s : tip)
			consumer.addLine(s);
		if(hasProgressBars(player))
			for(var bar : getProgressBars(player))
				consumer.addBar(bar);
	}
}