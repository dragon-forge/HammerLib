package com.zeitheron.hammercore.internal.init;

import com.zeitheron.hammercore.bookAPI.fancy.ManualCategories;
import com.zeitheron.hammercore.bookAPI.fancy.ManualEntry;
import com.zeitheron.hammercore.bookAPI.fancy.ManualEntry.EnumEntryShape;
import com.zeitheron.hammercore.bookAPI.fancy.pages.ManualTextPage;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ManualHC
{
	public static void register()
	{
		ManualCategories.registerCategory("hammercore", new ResourceLocation("hammercore", "textures/hammer.png"));
		
		new ManualEntry("hammercore", "hammercore", 0, 0, new ResourceLocation("hammercore", "textures/hammer.png")).setPages(new ManualTextPage("hc.manual_desc.hammercore")).setShape(EnumEntryShape.ROUND).registerEntry();
		
		if(ItemsHC.WRENCH != null)
			new ManualEntry("wrench", "hammercore", 0, 2, new ItemStack(ItemsHC.WRENCH)).setPages(new ManualTextPage("hc.manual_desc.wrench")).setShape(EnumEntryShape.ROUND).registerEntry();
	}
}