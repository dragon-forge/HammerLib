package com.pengu.hammercore.common.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * This class contains some general utilities for people
 */
public class HammerCoreUtils
{
	public static CreativeTabs createStaticIconCreativeTab(String name, final ItemStack iconStack)
	{
		return new CreativeTabs(name)
		{
			@Override
			public ItemStack getIconItemStack()
			{
				return iconStack.copy();
			}
			
			@Override
			public ItemStack getTabIconItem()
			{
				return iconStack.copy();
			}
		};
	}
	
	public static CreativeTabs createDynamicCreativeTab(String name, final int delayTicks)
	{
		return new CreativeTabs(name)
		{
			public ItemStack getIconItemStack()
			{
				NonNullList<ItemStack> items = NonNullList.create();
				displayAllRelevantItems(items);
				if(items.size() == 0)
					return new ItemStack(Blocks.BARRIER);
				
				// move to more-efficient + convenient methods.
				return items.get((int) ((Minecraft.getSystemTime() / (50L * delayTicks)) % items.size()));
			}
			
			@Override
			public ItemStack getTabIconItem()
			{
				return getIconItemStack();
			}
		};
	}
}