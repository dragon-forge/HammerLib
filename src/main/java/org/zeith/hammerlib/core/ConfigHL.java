package org.zeith.hammerlib.core;

import net.minecraftforge.api.distmarker.Dist;
import org.zeith.hammerlib.api.config.Config;
import org.zeith.hammerlib.api.config.ConfigHolder;
import org.zeith.hammerlib.api.config.IConfigRoot;
import org.zeith.hammerlib.api.config.IConfigStructure;

@Config
public class ConfigHL
		implements IConfigRoot
{
	public static ConfigHolder<ConfigHL> INSTANCE = new ConfigHolder<>();

	@Config.ConfigEntry(entry = "Internal", comment = "Internal features of HammerLib")
	public final ConfigHLInternal internal = new ConfigHLInternal();

	@Config.LoadOnlyIn(Dist.CLIENT)
	@Config.ConfigEntry(entry = "Client-Side", comment = "Client-side features of HammerLib")
	public final ConfigHLClientSide clientSide = new ConfigHLClientSide();

	public static class ConfigHLInternal
			implements IConfigStructure
	{
		@Config.ConfigEntry(entry = "Log HLB Events", comment = "Log HammerLib Event Bus Events? Don't enable this unless you know exactly what this does. Your logs will get spammed.")
		@Config.BooleanEntry(false)
		public boolean logHLBusEvents;
	}

	public static class ConfigHLClientSide
			implements IConfigStructure
	{
		@Config.ConfigEntry(entry = "LAN Port", comment = "Overrides LAN port. Zero will use any free available port. Also don't use ports like 1, or other low numbered ports. This will have no effect.")
		@Config.IntEntry(value = 0, min = 0, max = 65535)
		public int lanPort;
	}
}