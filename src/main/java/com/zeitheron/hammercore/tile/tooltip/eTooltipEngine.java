package com.zeitheron.hammercore.tile.tooltip;

import net.minecraftforge.fml.common.Loader;

public enum eTooltipEngine
{
	WAILA("waila"), //
	THEONEPROBE("theoneprobe");
	
	public final String modid;
	
	private eTooltipEngine(String modid)
	{
		this.modid = modid;
	}
	
	public boolean isInstalled()
	{
		return Loader.isModLoaded(modid);
	}
}