package com.zeitheron.hammercore.internal.items;

import net.minecraft.item.ItemStack;

/**
 * Used for colored enchanted items This class was generated 2017-09-20:22:24:03
 * 
 * @author APengu
 */
public interface ICustomEnchantColorItem
{
	int getEnchantEffectColor(ItemStack stack);
	
	default boolean shouldTruncateColorBrightness(ItemStack stack)
	{
		return true;
	}
}