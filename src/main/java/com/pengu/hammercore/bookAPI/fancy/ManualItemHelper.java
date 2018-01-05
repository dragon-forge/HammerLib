package com.pengu.hammercore.bookAPI.fancy;

import java.util.HashMap;

import com.pengu.hammercore.common.InterItemStack;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ManualItemHelper
{
	private static HashMap<int[], Object[]> keyCache = new HashMap();
	
	public static Object[] getCraftingRecipeKey(ItemStack stack)
	{
		if(InterItemStack.isStackNull(stack))
			return null;
		
		int[] key = new int[] { Item.getIdFromItem(stack.getItem()), stack.getMetadata() };
		
		if(keyCache.containsKey(key))
			keyCache.get(key);
		
		for(ManualCategory rcl : ManualCategories.manualCategories.values())
		{
			for(ManualEntry ri : rcl.entries.values())
			{
				if(ri.getPages() == null)
					continue;
				
				for(int a = 0; a < ri.getPages().length; ++a)
				{
					ManualPage page = ri.getPages()[a];
					
					page.getRecipe();
					boolean anyEqual = !InterItemStack.isStackNull(page.recipeOutput) && page.recipeOutput.isItemEqual(stack);
					if(!anyEqual)
						for(int i = 0; i < page.allOutputs.size(); ++i)
							if(!InterItemStack.isStackNull(page.allOutputs.get(i)) && page.allOutputs.get(i).isItemEqual(stack))
							{
								anyEqual = true;
								break;
							}
						
					if(!anyEqual)
						continue;
					
					Object[] c = new Object[] { ri.key, a };
					keyCache.put(key, c);
					
					return c;
				}
			}
		}
		keyCache.put(key, null);
		return null;
	}
}