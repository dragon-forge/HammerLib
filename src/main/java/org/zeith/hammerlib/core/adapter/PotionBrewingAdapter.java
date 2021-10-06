package org.zeith.hammerlib.core.adapter;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;

public class PotionBrewingAdapter
{
	/**
	 * Registers a brew to convert an input potion (with a help of a catalyst) into another potion.
	 */
	public static void addBrewingMix(Potion input, Item catalyst, Potion output)
	{
		PotionBrewing.addMix(input, catalyst, output);
	}
}