package org.zeith.hammerlib.tiles.tooltip.own;

public interface ITooltipProvider
{
	boolean isTooltipDirty();

	void setTooltipDirty(boolean dirty);

	void addInformation(ITooltip tip);
}