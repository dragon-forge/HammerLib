package com.zeitheron.hammercore.utils.recipes.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

/**
 * A Helper class to allow shapeless recipes to stay certain items that aren't
 * staying by default.
 */
public class ShapelessRecipeBuilder
{
	protected NonNullList<ItemStack[]> inputs = NonNullList.create();
	protected NonNullList<Integer> stay = NonNullList.create();
	protected Map<Integer, ItemStack> rets = new HashMap<>();
	protected ItemStack output = ItemStack.EMPTY;
	
	public static ShapelessRecipeBuilder builder()
	{
		return new ShapelessRecipeBuilder();
	}
	
	public ShapelessRecipeBuilder output(ItemStack stack)
	{
		this.output = stack.copy();
		return this;
	}
	
	public BuildableIngredient input()
	{
		return new BuildableIngredient(this);
	}
	
	public IRecipe build()
	{
		NonNullList<Ingredient> ings = NonNullList.withSize(inputs.size(), Ingredient.EMPTY);
		NonNullList<Ingredient> stay = NonNullList.withSize(inputs.size(), Ingredient.EMPTY);
		Map<Ingredient, ItemStack> returns = new HashMap<>();
		
		for(int i = 0; i < ings.size(); ++i)
		{
			Ingredient ing = Ingredient.fromStacks(inputs.get(i));
			ings.set(i, ing);
			if(this.stay.contains(i))
			{
				stay.set(i, ing);
				if(this.rets.containsKey(i))
					returns.put(ing, this.rets.get(i));
			}
		}
		
		return bake(ings, stay, returns);
	}
	
	protected IRecipe bake(NonNullList<Ingredient> ings, NonNullList<Ingredient> stay, Map<Ingredient, ItemStack> returns)
	{
		return new BakedShapelessRecipe(this, output, ings, stay, returns);
	}
	
	public static class BuildableIngredient
	{
		final ShapelessRecipeBuilder builder;
		Collection<ItemStack> inputs = new ArrayList<>();
		ItemStack returns;
		boolean stay;
		
		public BuildableIngredient(ShapelessRecipeBuilder builder)
		{
			this.builder = builder;
		}
		
		public BuildableIngredient addItem(ItemStack stack)
		{
			this.inputs.add(stack.copy());
			return this;
		}
		
		public BuildableIngredient addItems(Collection<ItemStack> stacks)
		{
			stacks.stream().filter(i -> i != null).forEach(this::addItem);
			return this;
		}
		
		public BuildableIngredient addOreDict(String od)
		{
			return addItems(OreDictionary.getOres(od));
		}
		
		public BuildableIngredient stay()
		{
			stay = true;
			return this;
		}
		
		public BuildableIngredient withReturn(ItemStack it)
		{
			this.returns = it;
			return this;
		}
		
		public ShapelessRecipeBuilder build()
		{
			if(!inputs.isEmpty())
			{
				if(stay || returns != null)
				{
					int i = builder.inputs.size();
					builder.stay.add(i);
					if(returns != null)
						builder.rets.put(i, returns);
				}
				builder.inputs.add(inputs.toArray(new ItemStack[inputs.size()]));
			}
			
			return builder;
		}
	}
	
	protected static class BakedShapelessRecipe extends ShapelessRecipes
	{
		private final ShapelessRecipeBuilder builder;
		private final NonNullList<Ingredient> stay;
		private final Map<Ingredient, ItemStack> returns;
		
		public BakedShapelessRecipe(ShapelessRecipeBuilder builder, ItemStack output, NonNullList<Ingredient> ingredients, NonNullList<Ingredient> stay, Map<Ingredient, ItemStack> returns)
		{
			super("", output, ingredients);
			this.returns = returns;
			this.builder = builder;
			this.stay = stay;
		}
		
		@Override
		public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
		{
			NonNullList<ItemStack> its = super.getRemainingItems(inv);
			for(int i = 0; i < its.size(); ++i)
			{
				ItemStack it = inv.getStackInSlot(i);
				for(Ingredient ing : stay)
					if(ing.apply(it))
					{
						if(returns.containsKey(ing))
						{
							ItemStack ret = returns.get(ing);
							if(ret != null)
							{
								ret = ret.copy();
								ret.setCount(it.getCount() * ret.getCount());
								its.set(i, ret);
							}
						} else
							its.set(i, it.copy());
						break;
					}
			}
			return its;
		}
	}
}