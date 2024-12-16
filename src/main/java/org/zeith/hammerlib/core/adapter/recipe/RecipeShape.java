package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.*;

import java.util.*;

public class RecipeShape
{
	public final HolderLookup.RegistryLookup<Item> itemRegistry;
	public final int width, height;
	public final List<String> shape;
	
	public RecipeShape(HolderLookup.RegistryLookup<Item> itemRegistry, int width, int height, String... shape)
	{
		this.itemRegistry = itemRegistry;
		
		if(height != shape.length)
			throw new IllegalArgumentException("Invalid height passed for shapeKeys; Expected " + height + ", got " + shape.length);
		
		for(int i = 0; i < shape.length; i++)
		{
			String key = shape[i];
			if(key.length() != width)
				throw new IllegalArgumentException("Invalid width passed at shapeKeys[" + i + "]=\"" + key + "\"; Expected " + width + ", got " + key.length());
		}
		
		this.width = width;
		this.height = height;
		this.shape = Arrays.asList(shape);
	}
	
	public RecipeShape(HolderLookup.RegistryLookup<Item> itemRegistry, String... shape)
	{
		this.itemRegistry = itemRegistry;
		
		this.width = shape[0].length();
		this.height = shape.length;
		this.shape = Arrays.asList(shape);
		for(int i = 0; i < shape.length; i++)
		{
			String key = shape[i];
			if(key.length() != width)
				throw new IllegalArgumentException("Invalid width passed at shape[" + i + "]=\"" + key + "\"; Expected " + width + ", got " + key.length());
		}
	}
	
	public List<Optional<Ingredient>> createIngredientMap(Map<Character, Ingredient> dictionary)
	{
		StringBuilder s = new StringBuilder();
		for(String s2 : shape) s.append(s2);
		List<Optional<Ingredient>> grid = NonNullList.withSize(width * height, Optional.empty());
		for(int l = 0; l < width * height; ++l)
		{
			char c0 = s.charAt(l);
			if(dictionary.containsKey(c0)) grid.set(l, Optional.ofNullable(dictionary.get(c0)));
			else grid.set(l, Optional.empty());
		}
		return grid;
	}
	
	public ShapedRecipePattern build(Map<Character, Ingredient> dictionary)
	{
		return new ShapedRecipePattern(width, height, createIngredientMap(dictionary), Optional.empty());
	}
}