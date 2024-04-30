package org.zeith.hammerlib.compat.top;

import mcjty.theoneprobe.api.*;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.tiles.tooltip.ITooltipConsumer;
import org.zeith.hammerlib.tiles.tooltip.ProgressBar;

public class TOPTooltipConsumer
		implements ITooltipConsumer
{
	protected final IProbeInfo info;
	protected final IProbeHitData hitData;
	
	public TOPTooltipConsumer(IProbeInfo info, IProbeHitData hitData)
	{
		this.info = info;
		this.hitData = hitData;
	}
	
	@Override
	public Vec3 getHitVec()
	{
		return hitData.getHitVec();
	}
	
	@Override
	public Direction getSideHit()
	{
		return hitData.getSideHit();
	}
	
	@Override
	public void addLine(Component text)
	{
		info.text(text);
	}
	
	@Override
	public void addItem(ItemStack stack)
	{
		info.item(stack);
	}
	
	@Override
	public void addBar(ProgressBar bar)
	{
		IProgressStyle style = info.defaultProgressStyle()
				.filledColor(bar.filledMainColor)
				.alternateFilledColor(bar.filledAlternateColor)
				.backgroundColor(bar.backgroundColor)
				.borderColor(bar.borderColor);
		if(bar.suffix != null && !bar.suffix.isEmpty())
			style = style.suffix(bar.suffix);
		if(bar.prefix != null && !bar.prefix.isEmpty())
			style = style.prefix(bar.prefix);
		if(bar.numberFormat != null)
			style = style.numberFormat(mcjty.theoneprobe.api.NumberFormat.values()[bar.numberFormat.ordinal()]);
		info.progress(bar.getProgressPercent(), 100, style);
	}
}