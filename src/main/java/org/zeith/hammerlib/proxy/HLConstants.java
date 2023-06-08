package org.zeith.hammerlib.proxy;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.init.ItemsHL;

public class HLConstants
{
	public static final Logger LOG = HammerLib.LOG;
	public static final String MOD_ID = "hammerlib";
	
	public static final Component CRAFTING_MATERIAL = Component.translatable("info.hammerlib.material").withStyle(ChatFormatting.GRAY);
	
	@CreativeTab.RegisterTab
	public static final CreativeTab HL_TAB = new CreativeTab(id("root"),
			b -> b.icon(() -> ItemsHL.WRENCH.getDefaultInstance())
					.title(Component.translatable("itemGroup." + MOD_ID))
	);
	
	public static ResourceLocation id(String path)
	{
		return new ResourceLocation(HLConstants.MOD_ID, path);
	}
}