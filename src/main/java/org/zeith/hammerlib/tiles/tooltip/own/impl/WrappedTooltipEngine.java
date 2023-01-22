package org.zeith.hammerlib.tiles.tooltip.own.impl;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.zeith.hammerlib.tiles.tooltip.ITooltipTile;
import org.zeith.hammerlib.tiles.tooltip.ProgressBar;
import org.zeith.hammerlib.tiles.tooltip.own.ITooltip;
import org.zeith.hammerlib.tiles.tooltip.own.ITooltipProvider;
import org.zeith.hammerlib.tiles.tooltip.own.inf.TooltipInfoProgressBar;
import org.zeith.hammerlib.tiles.tooltip.own.inf.TooltipInfoText;

import java.util.ArrayList;
import java.util.List;

public class WrappedTooltipEngine
		implements ITooltipProvider
{
	public long lastUpdateTime;
	private final BlockEntity tile;
	private final ITooltipTile ttt;
	
	protected long updateRate = 5L;
	
	public WrappedTooltipEngine(BlockEntity tile, ITooltipTile ttt)
	{
		this.tile = tile;
		this.ttt = ttt;
	}
	
	@Override
	public void addInformation(ITooltip tip)
	{
		var player = tip.getPlayer();
		
		List<Component> tooltip = new ArrayList<>();
		ttt.getTextTooltip(tooltip, player);
		tooltip.stream().map(TooltipInfoText::new).forEach(t -> tip.add(t).newLine());
		
		if(ttt.hasProgressBars(player))
		{
			var bars = ttt.getProgressBars(player);
			if(bars != null) for(ProgressBar bar : bars)
				tip.add(new TooltipInfoProgressBar(bar)).newLine();
		}
	}
	
	@Override
	public boolean isTooltipDirty()
	{
		return tile.hasLevel() && (tile.getLevel().getGameTime() - lastUpdateTime) > updateRate;
	}
	
	@Override
	public void setTooltipDirty(boolean dirty)
	{
		if(!dirty && tile.hasLevel())
		{
			lastUpdateTime = tile.getLevel().getGameTime();
		}
	}
}