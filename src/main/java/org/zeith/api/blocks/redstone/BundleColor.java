package org.zeith.api.blocks.redstone;

import com.google.common.base.Suppliers;
import net.minecraft.Util;
import net.minecraft.world.item.DyeColor;

import java.util.EnumMap;
import java.util.function.*;

public enum BundleColor
{
	WHITE(0x1),
	ORANGE(0x2),
	MAGENTA(0x4),
	LIGHT_BLUE(0x8),
	YELLOW(0x10),
	LIME(0x20),
	PINK(0x40),
	GRAY(0x80),
	LIGHT_GRAY(0x100),
	CYAN(0x200),
	PURPLE(0x400),
	BLUE(0x800),
	BROWN(0x1000),
	GREEN(0x2000),
	RED(0x4000),
	BLACK(0x8000);
	
	final int bitmask;
	
	BundleColor(int bitmask)
	{
		this.bitmask = bitmask;
	}
	
	public int bitmask()
	{
		return bitmask;
	}
	
	public static final Supplier<Function<DyeColor, BundleColor>> FROM_MC = Suppliers.memoize(() -> Util.make(new EnumMap<DyeColor, BundleColor>(DyeColor.class), m ->
	{
		m.put(DyeColor.WHITE, BundleColor.WHITE);
		m.put(DyeColor.ORANGE, BundleColor.ORANGE);
		m.put(DyeColor.MAGENTA, BundleColor.MAGENTA);
		m.put(DyeColor.LIGHT_BLUE, BundleColor.LIGHT_BLUE);
		m.put(DyeColor.YELLOW, BundleColor.YELLOW);
		m.put(DyeColor.LIME, BundleColor.LIME);
		m.put(DyeColor.PINK, BundleColor.PINK);
		m.put(DyeColor.GRAY, BundleColor.GRAY);
		m.put(DyeColor.LIGHT_GRAY, BundleColor.LIGHT_GRAY);
		m.put(DyeColor.CYAN, BundleColor.CYAN);
		m.put(DyeColor.PURPLE, BundleColor.PURPLE);
		m.put(DyeColor.BLUE, BundleColor.BLUE);
		m.put(DyeColor.BROWN, BundleColor.BROWN);
		m.put(DyeColor.GREEN, BundleColor.GREEN);
		m.put(DyeColor.RED, BundleColor.RED);
		m.put(DyeColor.BLACK, BundleColor.BLACK);
	})::get);
}
