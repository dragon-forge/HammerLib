package com.pengu.hammercore.common.inventory;

import net.minecraft.item.ItemStack;

public interface iSlotPredicate
{
	boolean test(int slot, ItemStack stack);
}