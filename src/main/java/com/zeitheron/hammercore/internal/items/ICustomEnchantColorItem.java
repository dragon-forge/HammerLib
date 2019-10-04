package com.zeitheron.hammercore.internal.items;

import net.minecraft.item.ItemStack;

public interface ICustomEnchantColorItem
{
	int getEnchantEffectColor(ItemStack stack);
	
	default boolean shouldTruncateColorBrightness(ItemStack stack)
	{
		return true;
	}
}