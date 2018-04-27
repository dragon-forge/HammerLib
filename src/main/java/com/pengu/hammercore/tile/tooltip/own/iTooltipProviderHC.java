package com.pengu.hammercore.tile.tooltip.own;

public interface iTooltipProviderHC
{
	boolean isTooltipDirty();
	
	void setTooltipDirty(boolean dirty);
	
	void addInformation(ITooltip tip);
}