package com.zeitheron.hammercore.client.utils;

import net.minecraft.item.ItemStack;

public interface IEnchantmentColorManager
{
	boolean applies(ItemStack stack);
	
	int apply(ItemStack stack, int prev);
	
	default boolean shouldTruncateColorBrightness(ItemStack stack)
	{
		return true;
	}
}