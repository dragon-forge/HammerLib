package org.zeith.hammerlib.tiles.tooltip.own.impl;

import net.minecraft.world.level.block.entity.BlockEntity;
import org.zeith.hammerlib.tiles.tooltip.ITooltipTile;
import org.zeith.hammerlib.tiles.tooltip.own.ITooltip;
import org.zeith.hammerlib.tiles.tooltip.own.ITooltipProvider;

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
		ttt.addTooltip(new HLTooltipConsumer(tip), player);
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