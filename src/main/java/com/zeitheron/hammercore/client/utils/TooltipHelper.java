package com.zeitheron.hammercore.client.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.energy.IEnergyStorage;

public class TooltipHelper
{
	private static final ThreadLocal<List<String>> TOOLTIP = ThreadLocal.withInitial(ArrayList::new);
	
	public static List<String> emptyTooltipList()
	{
		TOOLTIP.get().clear();
		return TOOLTIP.get();
	}
	
	public static List<String> generateEnergy(IEnergyStorage storage)
	{
		if(storage == null)
			return emptyTooltipList();
		return generate("energy", storage.getEnergyStored(), storage.getMaxEnergyStored(), "fe");
	}
	
	public static List<String> generate(String sub, int val, int max, String def)
	{
		return generateCustom("gui.hammercore." + sub, val, max, def);
	}
	
	public static List<String> generateCustom(String i18n, int value, int max, String def)
	{
		List<String> tip = TOOLTIP.get();
		tip.clear();
		tip.add(I18n.format(i18n));
		tip.add(String.format(TextFormatting.GRAY.toString() + "%,d" + (max > 0 ? " / %,d" : "") + " %s", max > 0 ? new Object[] { value, max, I18n.format("definition.hammercore:" + def + "." + (GuiScreen.isShiftKeyDown() ? "long" : "short")) } : new Object[] { value, I18n.format("definition.hammercore:" + def + "." + (GuiScreen.isShiftKeyDown() ? "long" : "short")) }));
		return tip;
	}
	
	public static String intToString(long num)
	{
		return String.format("%,d", num);
	}
}