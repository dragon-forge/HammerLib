package org.zeith.api.blocks.redstone;

import com.google.common.base.Suppliers;
import net.minecraft.Util;
import net.minecraft.world.item.DyeColor;

import java.util.EnumMap;
import java.util.function.*;

public enum MCColor
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
	
	MCColor(int bitmask)
	{
		this.bitmask = bitmask;
	}
	
	public int bitmask()
	{
		return bitmask;
	}
	
	public static final Supplier<Function<DyeColor, MCColor>> FROM_MC = Suppliers.memoize(() -> Util.make(new EnumMap<DyeColor, MCColor>(DyeColor.class), m ->
	{
		m.put(DyeColor.WHITE, MCColor.WHITE);
		m.put(DyeColor.ORANGE, MCColor.ORANGE);
		m.put(DyeColor.MAGENTA, MCColor.MAGENTA);
		m.put(DyeColor.LIGHT_BLUE, MCColor.LIGHT_BLUE);
		m.put(DyeColor.YELLOW, MCColor.YELLOW);
		m.put(DyeColor.LIME, MCColor.LIME);
		m.put(DyeColor.PINK, MCColor.PINK);
		m.put(DyeColor.GRAY, MCColor.GRAY);
		m.put(DyeColor.LIGHT_GRAY, MCColor.LIGHT_GRAY);
		m.put(DyeColor.CYAN, MCColor.CYAN);
		m.put(DyeColor.PURPLE, MCColor.PURPLE);
		m.put(DyeColor.BLUE, MCColor.BLUE);
		m.put(DyeColor.BROWN, MCColor.BROWN);
		m.put(DyeColor.GREEN, MCColor.GREEN);
		m.put(DyeColor.RED, MCColor.RED);
		m.put(DyeColor.BLACK, MCColor.BLACK);
	})::get);
}
