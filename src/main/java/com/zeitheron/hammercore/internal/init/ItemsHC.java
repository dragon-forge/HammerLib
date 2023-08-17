package com.zeitheron.hammercore.internal.init;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.annotations.*;
import com.zeitheron.hammercore.internal.items.ItemHammerCoreManual;
import com.zeitheron.hammercore.internal.items.ItemWrench;

import net.minecraft.item.Item;

@SimplyRegister(
		creativeTab = @FieldReference(clazz = HammerCore.class, field = "tab")
)
public class ItemsHC
{
	public static final Set<Item> items = new HashSet<>();
	public static final Set<Item> rendered_items = new HashSet<>();
	
	@RegistryName("manual")
	public static final ItemHammerCoreManual MANUAL = new ItemHammerCoreManual();
	
	@RegistryName("wrench")
	public static final ItemWrench WRENCH =
			Objects.equals(System.getProperty("hammercore.enableWrench"), "true") ? new ItemWrench() : null;
}