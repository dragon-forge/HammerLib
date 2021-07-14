package org.zeith.hammerlib.api.items;

import net.minecraft.item.ItemStack;

/**
 * WIP - we don't know how yet to implement the color modifier in Blaze3D engine.
 */
public interface IColoredGlintItem
{
	default int getNewGlintColor(ItemStack stack, int original)
	{
		return original;
	}
}