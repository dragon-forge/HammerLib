package com.zeitheron.hammercore.utils.inventory;

import net.minecraft.item.ItemStack;

public interface ISlotPredicate
{
	boolean test(int slot, ItemStack stack);
}