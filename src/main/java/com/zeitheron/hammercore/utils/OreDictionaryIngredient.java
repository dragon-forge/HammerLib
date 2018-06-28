package com.zeitheron.hammercore.utils;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryIngredient extends Ingredient
{
	private String od;
	private final NonNullList<ItemStack> matchingStackList;
	
	public OreDictionaryIngredient(String od)
	{
		this.od = od;
		
		/** Keep direct reference of matching stacks */
		matchingStackList = OreDictionary.getOres(od, true);
	}
	
	@Override
	public ItemStack[] getMatchingStacks()
	{
		return matchingStackList.toArray(new ItemStack[matchingStackList.size()]);
	}
	
	@Override
	public boolean apply(@Nullable ItemStack p_apply_1_)
	{
		if(p_apply_1_ == null)
		{
			return false;
		} else
		{
			for(ItemStack itemstack : this.matchingStackList)
			{
				if(itemstack.getItem() == p_apply_1_.getItem())
				{
					int i = itemstack.getMetadata();
					
					if(i == OreDictionary.WILDCARD_VALUE || i == p_apply_1_.getMetadata())
					{
						return true;
					}
				}
			}
			
			return false;
		}
	}
}