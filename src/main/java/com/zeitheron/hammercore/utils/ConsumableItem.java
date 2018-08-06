package com.zeitheron.hammercore.utils;

import java.util.Arrays;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;

public class ConsumableItem
{
	public final Ingredient ingr;
	
	public final int amount;
	
	public static ConsumableItem of(int amount, Ingredient... ing)
	{
		return new ConsumableItem(amount, ing);
	}
	
	public static ConsumableItem of(int amount, Item... items)
	{
		return new ConsumableItem(amount, Ingredient.fromItems(items));
	}
	
	public static ConsumableItem of(int amount, String oredict)
	{
		return new ConsumableItem(amount, new OreIngredient(oredict));
	}
	
	public static ConsumableItem of(int amount, ItemStack... stacks)
	{
		return new ConsumableItem(amount, Ingredient.fromStacks(stacks));
	}
	
	public ConsumableItem(int amount, Ingredient... ing)
	{
		this.amount = amount;
		this.ingr = Ingredient.merge(Arrays.asList(ing));
	}
	
	public boolean canConsume(IInventory inv)
	{
		int co = 0;
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack s = inv.getStackInSlot(i);
			if(!s.isEmpty() && ingr.apply(s))
				co += s.getCount();
		}
		return co >= amount;
	}
	
	public boolean consume(IInventory inv)
	{
		if(canConsume(inv))
		{
			int co = amount;
			for(int i = 0; i < inv.getSizeInventory() && co > 0; ++i)
			{
				ItemStack s = inv.getStackInSlot(i);
				if(!s.isEmpty() && ingr.apply(s))
				{
					int c = Math.min(co, s.getCount());
					s.shrink(c);
					co -= c;
				}
			}
			return co == 0;
		}
		return false;
	}
}