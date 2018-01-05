package com.pengu.hammercore.client.utils;

import net.minecraft.item.ItemStack;

public interface iEnchantmentColorManager
{
	boolean applies(ItemStack stack);
	
	int apply(ItemStack stack, int prev);
	
	default boolean shouldTruncateColorBrightness(ItemStack stack)
	{
		return true;
	}
}