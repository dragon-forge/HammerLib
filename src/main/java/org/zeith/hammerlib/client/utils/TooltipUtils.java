package org.zeith.hammerlib.client.utils;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class TooltipUtils
{
	public static final ResourceLocation ENERGY_SUB = new ResourceLocation("hammerlib", "energy");
	public static final ResourceLocation FE_DEFINITION = new ResourceLocation("hammerlib", "fe");

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
		return generate(ENERGY_SUB, storage.getEnergyStored(), storage.getMaxEnergyStored(), FE_DEFINITION);
	}

	public static List<String> generate(ResourceLocation sub, int val, int max, ResourceLocation definition)
	{
		return generateCustom("gui." + sub.getNamespace() + "." + sub.getPath(), val, max, definition);
	}

	public static List<String> generateCustom(String i18n, int value, int max, ResourceLocation definition)
	{
		List<String> tip = TOOLTIP.get();
		tip.clear();
		tip.add(I18n.get(i18n));
		tip.add(String.format(TextFormatting.GRAY.toString() + "%,d" + (max > 0 ? " / %,d" : "") + " %s", max > 0 ? new Object[]{
				value,
				max,
				I18n.get("definition." + definition.getNamespace() + ":" + definition.getPath() + "." + (Screen.hasShiftDown() ? "long" : "short"))
		} : new Object[]{
				value,
				I18n.get("definition." + definition.getNamespace() + ":" + definition.getPath() + "." + (Screen.hasShiftDown() ? "long" : "short"))
		}));
		return tip;
	}

	public static String intToString(long num)
	{
		return String.format("%,d", num);
	}
}