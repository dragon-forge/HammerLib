package org.zeith.hammerlib.proxy;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.items.ItemWrench;

public class HLConstants
{
	public static final Logger LOG = HammerLib.LOG;
	public static final String MOD_ID = "hammerlib";
	
	@CreativeTab.RegisterTab
	public static final CreativeTab HL_TAB = new CreativeTab(new ResourceLocation(HLConstants.MOD_ID, "root"),
			b -> b.icon(() -> ItemWrench.WRENCH.getDefaultInstance())
					.title(Component.translatable("itemGroup." + MOD_ID))
	);
}