package com.zeitheron.hammercore.internal.init;

import com.zeitheron.hammercore.bookAPI.fancy.ManualCategories;
import com.zeitheron.hammercore.bookAPI.fancy.ManualEntry;
import com.zeitheron.hammercore.bookAPI.fancy.ManualEntry.eEntryShape;
import com.zeitheron.hammercore.bookAPI.fancy.ManualPage;

import net.minecraft.util.ResourceLocation;

public class ManualHC
{
	public static void register()
	{
		ManualCategories.registerCategory("hammercore", new ResourceLocation("hammercore", "textures/hammer.png"), new ResourceLocation("hammercore", "textures/gui/manual_back.png"));
		
		new ManualEntry("hammercore", "hammercore", 0, 0, new ResourceLocation("hammercore", "textures/hammer.png")).setPages(new ManualPage("hc.manual_desc.hammercore")).setShape(eEntryShape.ROUND).registerEntry();
	}
}