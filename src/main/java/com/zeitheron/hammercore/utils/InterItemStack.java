package com.zeitheron.hammercore.utils;

import net.minecraft.item.ItemStack;

public class InterItemStack
{
	public static final ItemStack NULL_STACK = ItemStack.EMPTY;
	
	public static int getStackSize(ItemStack stack)
	{
		return stack.getCount();
	}
	
	public static void setStackSize(ItemStack stack, int size)
	{
		stack.setCount(size);
	}
	
	public static boolean isStackNull(ItemStack stack)
	{
		return stack == null || stack.isEmpty() || getStackSize(stack) <= 0 || stack == NULL_STACK;
	}
}