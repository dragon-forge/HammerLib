package org.zeith.hammerlib.core.recipes.replacers;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;

/**
 * An interface for replacing items in a crafting container.
 * Implementing lambdas (or classes) should provide the logic to determine the remaining item based on what is currently stored in the container's slot.
 * Additionally, all instances of {@link IRemainingItemReplacer} must be registered into the IForgeRegistry.
 * A good way of doing so would be @{@link org.zeith.hammerlib.annotations.SimplyRegister} with @{@link org.zeith.hammerlib.annotations.RegistryName}
 */
@FunctionalInterface
public interface IRemainingItemReplacer
{
	/**
	 * Returns the remaining item based on what is currently stored in the container's slot.
	 * If no modifications happen, the method should return the prevItem.
	 * The prevItem parameter is used when multiple instances of {@link IRemainingItemReplacer} are used sequentially.
	 * Depending on the order of replacers, they may perform various replacements on each other, or avoid replacing if the prevItem is not empty.
	 * A good reference point for the behavior of this method is the {@link WaterBottleReplacer},
	 * which takes into account previous replacers before replacing a water bottle with an empty glass bottle.
	 *
	 * @param container
	 * 		the crafting container
	 * @param slot
	 * 		the slot index in the container
	 * @param prevItem
	 * 		the previous item stack
	 *
	 * @return the remaining item stack, or prevItem if no modification should take place.
	 */
	ItemStack replace(CraftingContainer container, int slot, ItemStack prevItem);
}