package com.zeitheron.hammercore.internal.init;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.zeitheron.hammercore.internal.items.ItemHammerCoreManual;
import com.zeitheron.hammercore.internal.items.ItemWrench;

import net.minecraft.item.Item;

public class ItemsHC
{
	public static final Set<Item> items = new HashSet<Item>();
	public static final Set<Item> rendered_items = new HashSet<Item>();
	
	public static final ItemHammerCoreManual MANUAL = new ItemHammerCoreManual();
	public static final ItemWrench WRENCH = Objects.equals(System.getProperty("hammercore.enableWrench"), "true") ? new ItemWrench() : null;
}