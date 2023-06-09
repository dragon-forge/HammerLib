package org.zeith.hammerlib.proxy;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import org.apache.logging.log4j.Logger;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.init.ItemsHL;
import org.zeith.hammerlib.util.CommonMessages;

public class HLConstants
{
	public static final Logger LOG = HammerLib.LOG;
	public static final String MOD_ID = "hammerlib";
	
	@Deprecated // This should have not been placed here in the first place.
	public static final Component CRAFTING_MATERIAL = CommonMessages.CRAFTING_MATERIAL;
	
	@CreativeTab.RegisterTab
	public static final CreativeTab HL_TAB = new CreativeTab(id("root"),
			b -> b.icon(() -> ItemsHL.WRENCH.getDefaultInstance())
					.title(Component.translatable("itemGroup." + MOD_ID))
					.withTabsBefore(
							CreativeModeTabs.BUILDING_BLOCKS,
							CreativeModeTabs.COLORED_BLOCKS,
							CreativeModeTabs.NATURAL_BLOCKS,
							CreativeModeTabs.FUNCTIONAL_BLOCKS,
							CreativeModeTabs.REDSTONE_BLOCKS,
							CreativeModeTabs.TOOLS_AND_UTILITIES,
							CreativeModeTabs.COMBAT,
							CreativeModeTabs.FOOD_AND_DRINKS,
							CreativeModeTabs.INGREDIENTS,
							CreativeModeTabs.SPAWN_EGGS
					)
	);
	
	public static ResourceLocation id(String path)
	{
		return new ResourceLocation(HLConstants.MOD_ID, path);
	}
}