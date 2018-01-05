package com.pengu.hammercore.core.init;

import com.pengu.hammercore.bookAPI.fancy.ManualCategories;
import com.pengu.hammercore.bookAPI.fancy.ManualEntry;
import com.pengu.hammercore.bookAPI.fancy.ManualEntry.eEntryShape;
import com.pengu.hammercore.bookAPI.fancy.ManualPage;
import com.pengu.hammercore.bookAPI.fancy.ManualPage.PageType;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ManualHC
{
	public static void register()
	{
		ManualCategories.registerCategory("hammercore", new ResourceLocation("hammercore", "textures/hammer.png"), new ResourceLocation("hammercore", "textures/gui/manual_back.png"));
		
		new ManualEntry("hammercore", "hammercore", 0, 0, new ResourceLocation("hammercore", "textures/hammer.png")).setPages(new ManualPage("hc.manual_desc.hammercore")).setShape(eEntryShape.ROUND).registerEntry();
		new ManualEntry("calculatron", "hammercore", -5, -4, new ItemStack(ItemsHC.calculatron)).setPages(new ManualPage("hc.manual_desc.calculatron"), new ManualPage(PageType.NORMAL_CRAFTING, new ItemStack(ItemsHC.calculatron))).setParents("hammercore").registerEntry();
		new ManualEntry("wrench", "hammercore", -3, -4, new ItemStack(ItemsHC.wrench)).setPages(new ManualPage("hc.manual_desc.wrench"), new ManualPage(PageType.NORMAL_CRAFTING, new ItemStack(ItemsHC.wrench)), new ManualPage(PageType.NORMAL_CRAFTING, new ItemStack(ItemsHC.iron_gear))).setParents("hammercore").registerEntry();
		new ManualEntry("iwrench", "hammercore", -3, -6, new ItemStack(ItemsHC.iwrench)).setPages(new ManualPage("hc.manual_desc.iwrench"), new ManualPage(PageType.NORMAL_CRAFTING, new ItemStack(ItemsHC.iwrench))).setParents("wrench").setSpecial().registerEntry();
		new ManualEntry("chunk_loader", "hammercore", -1, -4, new ItemStack(BlocksHC.CHUNK_LOADER)).setPages(new ManualPage("hc.manual_desc.chunk_loader"), new ManualPage(PageType.NORMAL_CRAFTING, new ItemStack(BlocksHC.CHUNK_LOADER))).setShape(eEntryShape.HEX).setParents("hammercore").setSpecial().registerEntry();
		new ManualEntry("battery", "hammercore", 1, -4, new ItemStack(ItemsHC.battery)).setPages(new ManualPage("hc.manual_desc.battery"), new ManualPage(PageType.NORMAL_CRAFTING, new ItemStack(ItemsHC.battery))).setParents("hammercore").registerEntry();
	}
}