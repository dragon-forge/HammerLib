package org.zeith.hammerlib.compat.jade;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.tiles.tooltip.ITooltipConsumer;
import org.zeith.hammerlib.tiles.tooltip.ProgressBar;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.ui.*;
import snownee.jade.impl.ui.*;

import java.util.Optional;

public class JadeTooltipConsumer
		implements ITooltipConsumer
{
	protected final ITooltip tooltip;
	protected final BlockAccessor accessor;
	
	public JadeTooltipConsumer(ITooltip tooltip, BlockAccessor accessor)
	{
		this.tooltip = tooltip;
		this.accessor = accessor;
	}
	
	@Override
	public Vec3 getHitVec()
	{
		return accessor.getHitResult().getLocation();
	}
	
	@Override
	public Direction getSideHit()
	{
		return accessor.getSide();
	}
	
	@Override
	public void addLine(Component text)
	{
		tooltip.add(text);
	}
	
	@Override
	public void addItem(ItemStack stack)
	{
		tooltip.add(ItemStackElement.of(stack));
	}
	
	@Override
	public void addBar(ProgressBar bar)
	{
		SimpleProgressStyle style = new SimpleProgressStyle();
		
		BoxStyle.GradientBorder boxStyle = BoxStyle.GradientBorder.DEFAULT_NESTED_BOX.clone();
		boxStyle.roundCorner = false;
		boxStyle.bgColor = bar.backgroundColor;
		boxStyle.borderColor = new int[] { bar.borderColor, bar.borderColor, bar.borderColor, bar.borderColor };
		boxStyle.borderWidth = 1f;
		style.color = bar.filledMainColor;
		style.color2 = bar.filledAlternateColor;
		
		String core = switch(bar.numberFormat)
		{
			case COMPACT -> Math.round(bar.getProgress() * 100f) + " ";
			case NONE -> "";
			default -> Integer.toString(Math.round(bar.getProgress() * 100f));
		};
		
		String txt = (bar.prefix != null ? bar.prefix : "") + core + (bar.suffix != null ? bar.suffix : "");
		
		tooltip.add(new ProgressElement(bar.getProgress(), txt.isBlank() ? null : Component.literal(txt), style, boxStyle, false));
	}
}