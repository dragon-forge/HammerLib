package org.zeith.hammerlib.api.items;

import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.zeith.hammerlib.core.RecipeHelper;

import java.util.Arrays;

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
		return new ConsumableItem(amount, Ingredient.of(items));
	}

	public static ConsumableItem of(int amount, TagKey<Item> tag)
	{
		return new ConsumableItem(amount, RecipeHelper.fromTag(tag));
	}

	public static ConsumableItem of(int amount, ItemStack... stacks)
	{
		return new ConsumableItem(amount, Ingredient.of(stacks));
	}

	public ConsumableItem(int amount, Ingredient... ing)
	{
		this.amount = amount;
		this.ingr = Ingredient.merge(Arrays.asList(ing));
	}

	public boolean canConsume(Container inv)
	{
		int co = 0;
		for(int i = 0; i < inv.getContainerSize(); ++i)
		{
			ItemStack s = inv.getItem(i);
			if(!s.isEmpty() && ingr.test(s))
				co += s.getCount();
		}
		return co >= amount;
	}

	public boolean consume(Container inv)
	{
		if(canConsume(inv))
		{
			int co = amount;
			for(int i = 0; i < inv.getContainerSize() && co > 0; ++i)
			{
				ItemStack s = inv.getItem(i);
				if(!s.isEmpty() && ingr.test(s))
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