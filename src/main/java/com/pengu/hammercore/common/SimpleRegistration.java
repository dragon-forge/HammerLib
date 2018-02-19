package com.pengu.hammercore.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.pengu.hammercore.api.iNoItemBlock;
import com.pengu.hammercore.api.iTileBlock;
import com.pengu.hammercore.api.multipart.BlockMultipartProvider;
import com.pengu.hammercore.common.blocks.iItemBlock;
import com.pengu.hammercore.common.items.MultiVariantItem;
import com.pengu.hammercore.core.init.ItemsHC;
import com.pengu.hammercore.utils.SoundObject;
import com.pengu.hammercore.utils.iGetter;
import com.pengu.hammercore.utils.iRegisterListener;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class SimpleRegistration
{
	public static ShapedRecipes parseShapedRecipe(ItemStack stack, Object... recipeComponents)
	{
		ModContainer mc = Loader.instance().activeModContainer();
		String name = (mc != null ? mc.getModId() : "hammercore") + ":" + stack.getUnlocalizedName();
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		
		if(recipeComponents[i] instanceof String[])
		{
			String[] astring = ((String[]) recipeComponents[i++]);
			
			for(String s2 : astring)
			{
				++k;
				j = s2.length();
				s = s + s2;
			}
		} else
		{
			while(recipeComponents[i] instanceof String)
			{
				String s1 = (String) recipeComponents[i++];
				++k;
				j = s1.length();
				s = s + s1;
			}
		}
		
		Map<Character, ItemStack[]> map;
		
		for(map = Maps.<Character, ItemStack[]> newHashMap(); i < recipeComponents.length; i += 2)
		{
			Character character = (Character) recipeComponents[i];
			List<ItemStack> itemstack = new ArrayList<ItemStack>();
			
			if(recipeComponents[i + 1] instanceof Item)
				itemstack.add(new ItemStack((Item) recipeComponents[i + 1]));
			else if(recipeComponents[i + 1] instanceof Block)
				itemstack.add(new ItemStack((Block) recipeComponents[i + 1], 1, OreDictionary.WILDCARD_VALUE));
			else if(recipeComponents[i + 1] instanceof ItemStack)
				itemstack.add(((ItemStack) recipeComponents[i + 1]).copy());
			else if(recipeComponents[i + 1] instanceof String)
				itemstack.addAll(OreDictionary.getOres(recipeComponents[i + 1] + ""));
			else if(recipeComponents[i + 1] instanceof iGetter)
				itemstack.add(((iGetter<ItemStack>) recipeComponents[i + 1]).get());
			
			map.put(character, itemstack.toArray(new ItemStack[0]));
		}
		
		NonNullList<Ingredient> aitemstack = NonNullList.withSize(j * k, Ingredient.EMPTY);
		
		for(int l = 0; l < j * k; ++l)
		{
			char c0 = s.charAt(l);
			
			if(map.containsKey(Character.valueOf(c0)))
				aitemstack.set(l, Ingredient.fromStacks(map.get(Character.valueOf(c0))));
		}
		
		return new ShapedRecipes(name, j, k, aitemstack, stack);
	}
	
	/**
	 * This should only be used for registering recipes for vanilla objects and
	 * not mod-specific objects.
	 * 
	 * @param name
	 *            The name of the recipe.
	 * @param stack
	 *            The output stack.
	 * @param recipeComponents
	 *            The recipe components.
	 */
	public static ShapelessRecipes parseShapelessRecipe(ItemStack stack, Object... recipeComponents)
	{
		ModContainer mc = Loader.instance().activeModContainer();
		String name = (mc != null ? mc.getModId() : "hammercore") + ":" + stack.getUnlocalizedName();
		NonNullList<Ingredient> list = NonNullList.create();
		
		for(Object object : recipeComponents)
		{
			if(object instanceof ItemStack)
				list.add(Ingredient.fromStacks(((ItemStack) object).copy()));
			else if(object instanceof Item)
				list.add(Ingredient.fromStacks(new ItemStack((Item) object)));
			else if(object instanceof String)
				list.add(Ingredient.fromStacks(OreDictionary.getOres(object + "").toArray(new ItemStack[0])));
			else if(object instanceof iGetter)
				list.add(Ingredient.fromStacks(((iGetter<ItemStack>) object).get()));
			else
			{
				if(!(object instanceof Block))
					throw new IllegalArgumentException("Invalid shapeless recipe: unknown type " + object.getClass().getName() + "!");
				list.add(Ingredient.fromStacks(new ItemStack((Block) object)));
			}
		}
		
		return new ShapelessRecipes(name, stack, list);
	}
	
	public static void registerFieldItemsFrom(Class<?> owner, String modid, CreativeTabs tab)
	{
		Field[] fs = owner.getDeclaredFields();
		for(Field f : fs)
			if(Item.class.isAssignableFrom(f.getType()))
				try
				{
					f.setAccessible(true);
					registerItem((Item) f.get(null), modid, tab);
				} catch(Throwable err)
				{
				}
	}
	
	public static void registerFieldBlocksFrom(Class<?> owner, String modid, CreativeTabs tab)
	{
		Field[] fs = owner.getDeclaredFields();
		for(Field f : fs)
			if(Block.class.isAssignableFrom(f.getType()))
				try
				{
					f.setAccessible(true);
					registerBlock((Block) f.get(null), modid, tab);
				} catch(Throwable err)
				{
				}
	}
	
	public static void registerFieldSoundsFrom(Class<?> owner)
	{
		Field[] fs = owner.getDeclaredFields();
		for(Field f : fs)
			if(SoundObject.class.isAssignableFrom(f.getType()))
				try
				{
					f.setAccessible(true);
					registerSound((SoundObject) f.get(null));
				} catch(Throwable err)
				{
				}
	}
	
	/**
	 * Registers {@link SoundObject} to registry and populates
	 * {@link SoundObject} with {@link SoundEvent}.
	 **/
	public static void registerSound(SoundObject sound)
	{
		GameRegistry.findRegistry(SoundEvent.class).register(sound.sound = new SoundEvent(sound.name).setRegistryName(sound.name));
	}
	
	public static void registerItem(Item item, String modid, CreativeTabs tab)
	{
		if(item == null)
			return;
		String name = item.getUnlocalizedName().substring("item.".length());
		item.setRegistryName(modid, name);
		item.setUnlocalizedName(modid + ":" + name);
		if(tab != null)
			item.setCreativeTab(tab);
		GameRegistry.findRegistry(Item.class).register(item);
		if(item instanceof iRegisterListener)
			((iRegisterListener) item).onRegistered();
		if(item instanceof MultiVariantItem)
			ItemsHC.multiitems.add((MultiVariantItem) item);
		else
			ItemsHC.items.add(item);
	}
	
	public static void registerBlock(Block block, String modid, CreativeTabs tab)
	{
		if(block == null)
			return;
		String name = block.getUnlocalizedName().substring("tile.".length());
		block.setUnlocalizedName(modid + ":" + name);
		block.setCreativeTab(tab);
		
		// ItemBlockDefinition
		Item ib = null;
		
		if(block instanceof BlockMultipartProvider)
			ib = ((BlockMultipartProvider) block).createItem();
		else if(block instanceof iItemBlock)
			ib = ((iItemBlock) block).getItemBlock();
		else
			ib = new ItemBlock(block);
		
		block.setRegistryName(modid, name);
		GameRegistry.findRegistry(Block.class).register(block);
		if(!(block instanceof iNoItemBlock))
			GameRegistry.findRegistry(Item.class).register(ib.setRegistryName(block.getRegistryName()));
		
		if(block instanceof iRegisterListener)
			((iRegisterListener) block).onRegistered();
		
		if(block instanceof iTileBlock)
		{
			Class c = ((iTileBlock) block).getTileClass();
			
			// Better registration of tiles. Maybe this will fix tile
			// disappearing?
			GameRegistry.registerTileEntity(c, modid + ":" + c.getName().substring(c.getName().lastIndexOf(".") + 1).toLowerCase());
		} else if(block instanceof ITileEntityProvider)
		{
			ITileEntityProvider te = (ITileEntityProvider) block;
			TileEntity t = te.createNewTileEntity(null, 0);
			if(t != null)
			{
				Class c = t.getClass();
				GameRegistry.registerTileEntity(c, modid + ":" + c.getName().substring(c.getName().lastIndexOf(".") + 1).toLowerCase());
			}
		}
		
		if(!(block instanceof iNoItemBlock))
		{
			Item i = Item.getItemFromBlock(block);
			if(i instanceof iRegisterListener)
				((iRegisterListener) i).onRegistered();
			if(i instanceof MultiVariantItem)
				ItemsHC.multiitems.add((MultiVariantItem) i);
			else if(i != null)
				ItemsHC.items.add(i);
		}
	}
}