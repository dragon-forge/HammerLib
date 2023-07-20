package org.zeith.hammerlib.proxy;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.Logger;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.core.items.ItemWrench;

public class HLConstants
{
	public static final Logger LOG = HammerLib.LOG;
	public static final String MOD_ID = "hammerlib";
	
	public static final CreativeModeTab HL_TAB = new CreativeModeTab(MOD_ID)
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(ItemWrench.WRENCH);
		}
	};
	
	public static ResourceLocation id(String id)
	{
		return new ResourceLocation(MOD_ID, id);
	}
}