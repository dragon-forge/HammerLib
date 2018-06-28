package com.zeitheron.hammercore.tile.tooltip.own;

public interface ITooltipProviderHC
{
	boolean isTooltipDirty();
	
	void setTooltipDirty(boolean dirty);
	
	void addInformation(ITooltip tip);
}