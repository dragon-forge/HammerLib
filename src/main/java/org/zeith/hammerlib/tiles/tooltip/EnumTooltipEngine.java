package org.zeith.hammerlib.tiles.tooltip;

import net.neoforged.fml.ModList;

public enum EnumTooltipEngine
{
	WAILA("waila"),
	THEONEPROBE("theoneprobe"),
	HAMMER_LIB("hammerlib");
	
	public final String modid;
	
	EnumTooltipEngine(String modid)
	{
		this.modid = modid;
	}
	
	public boolean isInstalled()
	{
		return ModList.get().isLoaded(modid);
	}
}