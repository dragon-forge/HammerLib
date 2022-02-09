package org.zeith.hammerlib.tiles.tooltip.own;

public interface ITooltipProviderHC
{
	boolean isTooltipDirty();

	void setTooltipDirty(boolean dirty);

	void addInformation(ITooltip tip);
}