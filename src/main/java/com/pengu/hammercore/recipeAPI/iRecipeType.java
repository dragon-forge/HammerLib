package com.pengu.hammercore.recipeAPI;

import java.util.ArrayList;
import java.util.List;

import com.pengu.hammercore.common.utils.StringToItemStack;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.oredict.OreDictionary;

public interface iRecipeType<T>
{
	/**
	 * Indicates if HC should add/remove created instance of recipe to JEI.
	 */
	boolean isJeiSupported(T recipe);
	
	/**
	 * Returns the recipe that should be added to JEIRecipeRegistry
	 * 
	 * @deprecated Use {@link #getJeiRecipeFor(Object, boolean)} instead.
	 */
	@Deprecated
	default Object getJeiRecipeFor(T recipe)
	{
		return null;
	};
	
	/**
	 * Returns the recipe that should be added to/removed from JEIRecipeRegistry
	 */
	default Object getJeiRecipeFor(T recipe, boolean remove)
	{
		return getJeiRecipeFor(recipe);
	}
	
	String getTypeId();
	
	T createRecipe(NBTTagCompound json) throws RecipeParseException;
	
	void addRecipe(T recipe);
	
	void removeRecipe(T recipe);
	
	/**
	 * Checks to see if this recipe's add and remove methods could be swapped.
	 * Used to remove recipes.
	 */
	default boolean swapAddRemoveSupported(T recipe)
	{
		return false;
	};
	
	/**
	 * Called instead of {@link iRecipeType#addRecipe(T)} if
	 * {@link iRecipeType#swapAddRemoveSupported(Object)} returns true.
	 */
	default void removeOnLoad(T recipe)
	{
	};
	
	/**
	 * Called instead of {@link iRecipeType#addRecipe(T)} if
	 * {@link iRecipeType#swapAddRemoveSupported(Object)} returns true.
	 */
	default void addOnUnload(T recipe)
	{
	};
	
	default ItemStack parseStack(String stack, String nbt)
	{
		return StringToItemStack.toItemStack(stack, nbt);
	}
	
	default ItemStack loadStack(NBTTagCompound nbt)
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("id", nbt.getString("id"));
		
		if(nbt.hasKey("count"))
			compound.setByte("Count", (byte) nbt.getInteger("count"));
		else
			compound.setByte("Count", (byte) 1);
		
		if(nbt.hasKey("damage"))
		{
			if(nbt.getInteger("damage") < 0)
				compound.setShort("Damage", (short) OreDictionary.WILDCARD_VALUE);
			else
				compound.setShort("Damage", (short) nbt.getInteger("damage"));
		}
		
		if(nbt.hasKey("tag"))
			compound.setTag("tag", nbt.getCompoundTag("tag"));
		if(nbt.hasKey("ForgeCaps"))
			compound.setTag("ForgeCaps", nbt.getCompoundTag("ForgeCaps"));
		return new ItemStack(compound);
	}
	
	default List<ItemStack> loadStacks(NBTBase base, String item)
	{
		List<ItemStack> stacks = new ArrayList<>();
		if(base == null)
			return stacks;
		if(base instanceof NBTTagCompound)
			stacks.add(loadStack((NBTTagCompound) base));
		else if(base instanceof NBTTagString)
			stacks.addAll(OreDictionary.getOres(((NBTTagString) base).getString()));
		else if(base instanceof NBTTagList && ((NBTTagList) base).getTagType() == NBT.TAG_COMPOUND)
			for(int i = 0; i < ((NBTTagList) base).tagCount(); ++i)
			{
				NBTTagCompound nbt = ((NBTTagList) base).getCompoundTagAt(i);
				stacks.add(loadStack(nbt));
			}
		else if(base instanceof NBTTagList && ((NBTTagList) base).getTagType() == NBT.TAG_STRING)
			for(int i = 0; i < ((NBTTagList) base).tagCount(); ++i)
			{
				String od = ((NBTTagList) base).getStringTagAt(i);
				stacks.addAll(OreDictionary.getOres(od));
			}
		else
			throw new RecipeParseException("Undefined type for ingredient '" + item + "': TagID: " + base.getId() + ", Content: " + base);
		return stacks;
	}
	
	public static class RecipeParseException extends RuntimeException
	{
		public RecipeParseException(String msg)
		{
			super(msg);
		}
	}
}