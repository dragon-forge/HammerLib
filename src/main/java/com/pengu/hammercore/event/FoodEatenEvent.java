package com.pengu.hammercore.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

/**
 * Called when entity is done eating a {@link ItemFood}
 */
public class FoodEatenEvent extends ItemUseFinishEvent
{
	public FoodEatenEvent(EntityLivingBase living, ItemStack stack)
	{
		super(living, stack);
	}
}