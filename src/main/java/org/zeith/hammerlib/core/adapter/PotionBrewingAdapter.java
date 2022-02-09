package org.zeith.hammerlib.core.adapter;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;

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