package com.zeitheron.hammercore.internal.init;

import com.zeitheron.hammercore.bookAPI.fancy.ManualCategories;
import com.zeitheron.hammercore.bookAPI.fancy.ManualEntry;
import com.zeitheron.hammercore.bookAPI.fancy.ManualEntry.EnumEntryShape;
import com.zeitheron.hammercore.bookAPI.fancy.ManualEntry.eEntryShape;
import com.zeitheron.hammercore.bookAPI.fancy.ManualPage;
import com.zeitheron.hammercore.utils.web.URLLocation;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ManualHC
{
	public static void register()
	{
		ManualCategories.registerCategory("hammercore", new ResourceLocation("hammercore", "textures/hammer.png"));
		
		new ManualEntry("hammercore", "hammercore", 0, 0, new ResourceLocation("hammercore", "textures/hammer.png")).setPages(new ManualPage("hc.manual_desc.hammercore")).setShape(EnumEntryShape.ROUND).registerEntry();
		
		if(ItemsHC.WRENCH != null)
			new ManualEntry("wrench", "hammercore", 0, 2, new ItemStack(ItemsHC.WRENCH)).setPages(new ManualPage("hc.manual_desc.wrench")).setShape(EnumEntryShape.ROUND).registerEntry();
		
		new ManualEntry("apengu", "hammercore", -1, -2, new URLLocation("https://visage.surgeplay.com/head/256/4249ed58d13c4a89a9da1e507774dfac")).setPages(new ManualPage("hc.manual_desc.apengu.1"), new ManualPage("hc.manual_desc.apengu.2"), new ManualPage("hc.manual_desc.apengu.3")).setShape(EnumEntryShape.HEX).setColor(0xFFFF0000).registerEntry();
		new ManualEntry("zeitheron", "hammercore", 1, -2, new URLLocation("https://visage.surgeplay.com/head/256/63fa4c485b5640809727d5d59663e603")).setPages(new ManualPage("hc.manual_desc.zeitheron.1"), new ManualPage("hc.manual_desc.zeitheron.2")).setShape(EnumEntryShape.HEX).setColor(0xFF00FFFF).registerEntry();
	}
}