package com.zeitheron.hammercore.utils.match.item;

import java.util.List;

import com.google.common.base.Predicate;

import net.minecraft.item.ItemStack;

public class ItemListContainerHelper
{
	public static boolean containsItem(List<ItemStack> items, ItemStack target, ItemMatchParams params)
	{
		ItemContainer con = ItemContainer.create(target);
		for(ItemStack stack : items)
			if(con.matches(stack, params))
				return true;
		return false;
	}
	
	public static boolean containsItem(List<ItemStack> items, String target, ItemMatchParams params)
	{
		ItemContainer con = ItemContainer.create(target);
		for(ItemStack stack : items)
			if(con.matches(stack, params))
				return true;
		return false;
	}
	
	public static Predicate<ItemStack> stackPredicate(List<ItemStack> items, ItemMatchParams params)
	{
		return new Predicate<ItemStack>()
		{
			@Override
			public boolean apply(ItemStack input)
			{
				return containsItem(items, input, params);
			}
		};
	}
	
	public static Predicate<String> oredictPredicate(List<ItemStack> items, ItemMatchParams params)
	{
		return new Predicate<String>()
		{
			@Override
			public boolean apply(String input)
			{
				return containsItem(items, input, params);
			}
		};
	}
}