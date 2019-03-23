package com.zeitheron.hammercore.internal;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.common.collect.Maps;
import com.zeitheron.hammercore.annotations.RecipeRegister;
import com.zeitheron.hammercore.api.INoItemBlock;
import com.zeitheron.hammercore.api.ITileBlock;
import com.zeitheron.hammercore.api.multipart.BlockMultipartProvider;
import com.zeitheron.hammercore.internal.blocks.IItemBlock;
import com.zeitheron.hammercore.internal.init.ItemsHC;
import com.zeitheron.hammercore.utils.IRegisterListener;
import com.zeitheron.hammercore.utils.SoundObject;
import com.zeitheron.hammercore.utils.WorldUtil;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreIngredient;

public class SimpleRegistration
{
	public static ShapedRecipes parseShapedRecipe(ItemStack stack, Object... recipeComponents)
	{
		ModContainer mc = Loader.instance().activeModContainer();
		String name = (mc != null ? mc.getModId() : "hammercore") + ":" + stack.getTranslationKey();
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
		
		Map<Character, Ingredient> map;
		
		for(map = Maps.<Character, Ingredient> newHashMap(); i < recipeComponents.length; i += 2)
		{
			Character character = (Character) recipeComponents[i];
			Ingredient ingr = null;
			
			if(recipeComponents[i + 1] instanceof Item)
				ingr = Ingredient.fromItem((Item) recipeComponents[i + 1]);
			else if(recipeComponents[i + 1] instanceof Block)
				ingr = Ingredient.fromItem(Item.getItemFromBlock((Block) recipeComponents[i + 1]));
			else if(recipeComponents[i + 1] instanceof ItemStack)
				ingr = Ingredient.fromStacks(((ItemStack) recipeComponents[i + 1]).copy());
			else if(recipeComponents[i + 1] instanceof ItemStack[])
			{
				ItemStack[] items = ((ItemStack[]) recipeComponents[i + 1]).clone();
				for(int l = 0; l < items.length; ++l)
					items[l] = items[l].copy();
				ingr = Ingredient.fromStacks(items);
			} else if(recipeComponents[i + 1] instanceof String)
				ingr = new OreIngredient(recipeComponents[i + 1] + "");
			else if(recipeComponents[i + 1] instanceof Ingredient)
				ingr = (Ingredient) recipeComponents[i + 1];
			
			map.put(character, ingr);
		}
		
		NonNullList<Ingredient> aitemstack = NonNullList.withSize(j * k, Ingredient.EMPTY);
		
		for(int l = 0; l < j * k; ++l)
		{
			char c0 = s.charAt(l);
			
			if(map.containsKey(Character.valueOf(c0)))
				aitemstack.set(l, map.get(Character.valueOf(c0)));
		}
		
		return new ShapedRecipes(name, j, k, aitemstack, stack);
	}
	
	/**
	 * This should only be used for registering recipes for vanilla objects and
	 * not mod-specific objects.
	 * 
	 * @param stack
	 *            The output stack.
	 * @param recipeComponents
	 *            The recipe components.
	 * @return The parsed recipe
	 */
	public static ShapelessRecipes parseShapelessRecipe(ItemStack stack, Object... recipeComponents)
	{
		ModContainer mc = Loader.instance().activeModContainer();
		String name = (mc != null ? mc.getModId() : "hammercore") + ":" + stack.getTranslationKey();
		NonNullList<Ingredient> list = NonNullList.create();
		
		for(Object object : recipeComponents)
		{
			Ingredient ingr = null;
			
			if(object instanceof Item)
				ingr = Ingredient.fromItem((Item) object);
			else if(object instanceof Block)
				ingr = Ingredient.fromItem(Item.getItemFromBlock((Block) object));
			else if(object instanceof ItemStack)
				ingr = Ingredient.fromStacks(((ItemStack) object).copy());
			else if(object instanceof ItemStack[])
			{
				ItemStack[] items = ((ItemStack[]) object).clone();
				for(int l = 0; l < items.length; ++l)
					items[l] = items[l].copy();
				ingr = Ingredient.fromStacks(items);
			} else if(object instanceof String)
				ingr = new OreIngredient(object + "");
			else if(object instanceof Ingredient)
				ingr = (Ingredient) object;
			
			if(ingr != null)
				list.add(ingr);
			else
				throw new IllegalArgumentException("Invalid shapeless recipe: unknown type " + object.getClass().getName() + "!");
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
	 * 
	 * @param sound
	 *            The object containing a pahth to sound object
	 **/
	public static void registerSound(SoundObject sound)
	{
		GameRegistry.findRegistry(SoundEvent.class).register(sound.sound = new SoundEvent(sound.name).setRegistryName(sound.name));
	}
	
	public static void registerItem(Item item, String modid, CreativeTabs tab)
	{
		if(item == null)
			return;
		String name = item.getTranslationKey().substring("item.".length());
		item.setRegistryName(modid, name);
		item.setTranslationKey(modid + ":" + name);
		if(tab != null)
			item.setCreativeTab(tab);
		ForgeRegistries.ITEMS.register(item);
		if(item instanceof IRegisterListener)
			((IRegisterListener) item).onRegistered();
		
		ItemsHC.items.add(item);
	}
	
	public static void registerBlock(Block block, String modid, CreativeTabs tab)
	{
		if(block == null)
			return;
		String name = block.getTranslationKey().substring("tile.".length());
		block.setTranslationKey(modid + ":" + name);
		block.setCreativeTab(tab);
		
		// ItemBlockDefinition
		Item ib = null;
		
		if(block instanceof BlockMultipartProvider)
			ib = ((BlockMultipartProvider) block).createItem();
		else if(block instanceof IItemBlock)
			ib = ((IItemBlock) block).getItemBlock();
		else
			ib = new ItemBlock(block);
		
		block.setRegistryName(modid, name);
		ForgeRegistries.BLOCKS.register(block);
		if(!(block instanceof INoItemBlock))
			ForgeRegistries.ITEMS.register(ib.setRegistryName(block.getRegistryName()));
		
		if(block instanceof IRegisterListener)
			((IRegisterListener) block).onRegistered();
		
		if(block instanceof ITileBlock)
		{
			Class c = ((ITileBlock) block).getTileClass();
			
			// Better registration of tiles. Maybe this will fix tile
			// disappearing?
			TileEntity.register(modid + ":" + c.getName().substring(c.getName().lastIndexOf(".") + 1).toLowerCase(), c);
		} else if(block instanceof ITileEntityProvider)
		{
			ITileEntityProvider te = (ITileEntityProvider) block;
			TileEntity t = te.createNewTileEntity(null, 0);
			if(t != null)
			{
				Class c = t.getClass();
				TileEntity.register(modid + ":" + c.getName().substring(c.getName().lastIndexOf(".") + 1).toLowerCase(), c);
			}
		}
		
		if(!(block instanceof INoItemBlock))
		{
			Item i = Item.getItemFromBlock(block);
			if(i instanceof IRegisterListener)
				((IRegisterListener) i).onRegistered();
			if(i != null)
				ItemsHC.items.add(i);
		}
	}
	
	private static final List<Supplier<List<IRecipe>>> RECIPE_GENERATORS = new ArrayList<>();
	
	public static void registerConstantRecipes(Class<?> base)
	{
		for(Method m : base.getDeclaredMethods())
		{
			int mod = m.getModifiers();
			if(Modifier.isStatic(mod) && m.getAnnotation(RecipeRegister.class) != null && m.getParameterTypes().length == 1 && List.class.isAssignableFrom(m.getParameterTypes()[0]))
			{
				Type type = m.getParameters()[0].getParameterizedType();
				if(type instanceof ParameterizedType)
				{
					type = ((ParameterizedType) type).getActualTypeArguments()[0];
					if(Class.class.isAssignableFrom(type.getClass()) && IRecipe.class.getName().equals(type.getTypeName()))
					{
						m.setAccessible(true);
						final Method $ = m;
						
						RECIPE_GENERATORS.add(() ->
						{
							List<IRecipe> recipes = new ArrayList<>();
							try
							{
								$.invoke(null, recipes);
							} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
							{
								e.printStackTrace();
							}
							return recipes;
						});
					}
				}
			}
		}
	}
	
	public static void $addRegisterRecipes(Consumer<IRecipe> registry)
	{
		for(Supplier<List<IRecipe>> recipes : RECIPE_GENERATORS)
			recipes.get().forEach(registry);
	}
}