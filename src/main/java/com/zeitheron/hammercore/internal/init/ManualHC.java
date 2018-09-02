package com.zeitheron.hammercore.internal.init;

import com.zeitheron.hammercore.bookAPI.fancy.ManualCategories;
import com.zeitheron.hammercore.bookAPI.fancy.ManualEntry;
import com.zeitheron.hammercore.bookAPI.fancy.ManualEntry.EnumEntryShape;
import com.zeitheron.hammercore.bookAPI.fancy.pages.ManualTextPage;
import com.zeitheron.hammercore.bookAPI.fancy.ManualPage;
import com.zeitheron.hammercore.utils.web.URLLocation;

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
		
		new ManualEntry("apengu", "hammercore", -1, -2, new URLLocation("https://visage.surgeplay.com/head/256/4249ed58d13c4a89a9da1e507774dfac")).setPages(new ManualTextPage("hc.manual_desc.apengu")).setShape(EnumEntryShape.HEX).setColor(0xFFFF0000).registerEntry();
		new ManualEntry("zeitheron", "hammercore", 1, -2, new URLLocation("https://visage.surgeplay.com/head/256/63fa4c485b5640809727d5d59663e603")).setPages(new ManualTextPage("hc.manual_desc.zeitheron")).setShape(EnumEntryShape.HEX).setColor(0xFF00FFFF).registerEntry();
	}
}