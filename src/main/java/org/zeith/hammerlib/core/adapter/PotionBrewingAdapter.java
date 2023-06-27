package org.zeith.hammerlib.core.adapter;

import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import org.zeith.hammerlib.mixins.PotionBrewingAccessor;

public class PotionBrewingAdapter
{
	/**
	 * Registers a brew to convert an input potion (with a help of a catalyst) into another potion.
	 */
	public static void addBrewingMix(Potion input, Item catalyst, Potion output)
	{
		PotionBrewing.addMix(input, catalyst, output);
	}
	
	/**
	 * Registers a container conversion while keeping the NBT tags (with a help of a catalyst) into another container.
	 */
	public static void addContainerMix(Item input, Item catalyst, Item output)
	{
		PotionBrewingAccessor.getContainerMixes()
							 .add(new PotionBrewing.Mix<>(net.minecraftforge.registries.ForgeRegistries.ITEMS, input, Ingredient.of(catalyst), output));
	}
	
	/**
	 * Registers an item as a container
	 */
	public static void addContainer(Ingredient container)
	{
		PotionBrewingAccessor.getAllowedContainers().add(container);
	}
}