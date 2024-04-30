package org.zeith.hammerlib.tiles.tooltip.own.impl;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.tiles.tooltip.ITooltipConsumer;
import org.zeith.hammerlib.tiles.tooltip.ProgressBar;
import org.zeith.hammerlib.tiles.tooltip.own.ITooltip;

public class HLTooltipConsumer
		implements ITooltipConsumer
{
	protected final ITooltip tooltip;
	
	public HLTooltipConsumer(ITooltip tooltip)
	{
		this.tooltip = tooltip;
	}
	
	@Override
	public Vec3 getHitVec()
	{
		return tooltip.getHitVec();
	}
	
	@Override
	public Direction getSideHit()
	{
		return tooltip.getSideHit();
	}
	
	@Override
	public void addLine(Component text)
	{
		tooltip.addText(text).newLine().addSpacing(0, 3).newLine();
	}
	
	@Override
	public void addItem(ItemStack stack)
	{
		tooltip.addStack(stack, 16, 16).newLine().addSpacing(0, 3).newLine();
	}
	
	@Override
	public void addBar(ProgressBar bar)
	{
		tooltip.addProgressBar(bar).newLine().addSpacing(0, 3).newLine();
	}
}