package org.zeith.hammerlib.api.items.tooltip;

import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.zeith.hammerlib.util.colors.ColorHelper;

import java.util.Comparator;

public class TooltipColoredLine
		implements TooltipComponent
{
	private final IntList colors;
	
	public TooltipColoredLine(int[] colors)
	{
		this.colors = new IntArrayList(colors);
		this.colors.sort(IntComparators.asIntComparator(
				Comparator.comparingInt(this::getHue)
						.thenComparingDouble(ColorHelper::luma)
		));
	}
	
	public int getHue(int rgb)
	{
		int red = ColorHelper.getRedi(rgb), green = ColorHelper.getGreeni(rgb), blue = ColorHelper.getBluei(rgb);
		
		float min = Math.min(Math.min(red, green), blue);
		float max = Math.max(Math.max(red, green), blue);
		
		if(min == max)
		{
			return 0;
		}
		
		float hue;
		if(max == red) hue = (green - blue) / (max - min);
		else if(max == green) hue = 2f + (blue - red) / (max - min);
		else hue = 4f + (red - green) / (max - min);
		
		hue = hue * 60;
		if(hue < 0) hue = hue + 360;
		
		return Math.round(hue);
	}
	
	public IntList getColors()
	{
		return colors;
	}
}