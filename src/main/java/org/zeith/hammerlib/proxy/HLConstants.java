package org.zeith.hammerlib.proxy;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.core.adapter.CreativeTabAdapter;
import org.zeith.hammerlib.core.items.ItemWrench;

public class HLConstants
{
	public static final Logger LOG = HammerLib.LOG;
	public static final String MOD_ID = "hammerlib";
	
	public static final CreativeTabAdapter.CreativeTab HL_TAB = CreativeTabAdapter.create(new ResourceLocation(HLConstants.MOD_ID, "root"),
			b -> b.icon(() -> ItemWrench.WRENCH.getDefaultInstance())
	);
}