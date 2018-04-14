package com.pengu.hammercore.core.init;

import java.util.HashSet;
import java.util.Set;

import com.pengu.hammercore.cfg.HammerCoreConfigs;
import com.pengu.hammercore.common.items.ItemBattery;
import com.pengu.hammercore.common.items.ItemCalculatron;
import com.pengu.hammercore.common.items.ItemHammerCoreManual;
import com.pengu.hammercore.common.items.ItemIWrench;
import com.pengu.hammercore.common.items.ItemWrench;
import com.pengu.hammercore.common.items.MultiVariantItem;
import com.pengu.hammercore.common.items.debug.ItemRayTracer;
import com.pengu.hammercore.common.items.debug.ItemZapper;

import net.minecraft.item.Item;

public class ItemsHC
{
	public static final Set<Item> items = new HashSet<Item>();
	public static final Set<Item> rendered_items = new HashSet<Item>();
	public static final Set<MultiVariantItem> multiitems = new HashSet<MultiVariantItem>();
	
	public static final Item RAY_TRACER, //
	        ZAPPER, //
	        CALCULATRON = new ItemCalculatron(), //
	        MANUAL = new ItemHammerCoreManual(), //
	        BATTERY, //
	        WRENCH = new ItemWrench(), //
	        IWRENCH = new ItemIWrench(), //
	        IRON_GEAR = new Item().setMaxStackSize(16).setUnlocalizedName("iron_gear");
	
	static
	{
		BATTERY = HammerCoreConfigs.items_addAccumulator ? new ItemBattery(200_000, 25_000).setUnlocalizedName("battery") : null;
		RAY_TRACER = HammerCoreConfigs.debug_addRaytracer ? new ItemRayTracer() : null;
		ZAPPER = HammerCoreConfigs.debug_addZapper ? new ItemZapper() : null;
	}
}