package com.zeitheron.hammercore.internal.init;

import java.util.HashSet;
import java.util.Set;

import com.zeitheron.hammercore.internal.items.ItemHammerCoreManual;

import net.minecraft.item.Item;

public class ItemsHC
{
	public static final Set<Item> items = new HashSet<Item>();
	public static final Set<Item> rendered_items = new HashSet<Item>();
	
	public static final Item MANUAL = new ItemHammerCoreManual();
}