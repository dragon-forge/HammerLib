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
	public static final transient Set<Item> items = new HashSet<Item>();
	public static final transient Set<Item> rendered_items = new HashSet<Item>();
	public static final transient Set<MultiVariantItem> multiitems = new HashSet<MultiVariantItem>();
	
	public static final Item ray_tracer, //
	        zapper, //
	        calculatron = new ItemCalculatron(), //
	        manual = new ItemHammerCoreManual(), //
	        battery = new ItemBattery(2_000_000, 25_000).setUnlocalizedName("battery"), //
	        wrench = new ItemWrench(), //
	        iwrench = new ItemIWrench(), //
	        iron_gear = new Item().setMaxStackSize(16).setUnlocalizedName("iron_gear");
	
	static
	{
		if(HammerCoreConfigs.debug_addRaytracer)
			ray_tracer = new ItemRayTracer();
		else
			ray_tracer = null;
		
		if(HammerCoreConfigs.debug_addZapper)
			zapper = new ItemZapper();
		else
			zapper = null;
	}
}