package com.zeitheron.hammercore.utils.recipes.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.zeitheron.hammercore.internal.SimpleRegistration;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.oredict.OreDictionary;

public abstract class RecipeRegistry
{
	public abstract void crafting();
	
	/** Called during init phase. */
	public void smelting()
	{
		
	}
	
	/**
	 * Also called during init phase. Optional, use to register ore dictionary
	 */
	@Deprecated
	public void oredict()
	{
		
	}
	
	public static ItemStack enchantedBook(Enchantment ench, int lvl)
	{
		ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
		Map<Enchantment, Integer> enchs = new HashMap<>();
		enchs.put(ench, lvl);
		EnchantmentHelper.setEnchantments(enchs, book);
		return book;
	}
	
	public static ItemStack potionItemNormal(PotionType... types)
	{
		ItemStack po = new ItemStack(Items.POTIONITEM);
		for(PotionType type : types)
			PotionUtils.addPotionToItemStack(po, type);
		return po;
	}
	
	public static ItemStack potionItemSplash(PotionType... types)
	{
		ItemStack po = new ItemStack(Items.SPLASH_POTION);
		for(PotionType type : types)
			PotionUtils.addPotionToItemStack(po, type);
		return po;
	}
	
	public static ItemStack potionItemLingering(PotionType... types)
	{
		ItemStack po = new ItemStack(Items.LINGERING_POTION);
		for(PotionType type : types)
			PotionUtils.addPotionToItemStack(po, type);
		return po;
	}
	
	protected List<IRecipe> recipes = new ArrayList<>();
	
	public Collection<IRecipe> collect()
	{
		recipes = new ArrayList<>();
		crafting();
		HashSet<IRecipe> recipes = new HashSet<>(this.recipes);
		this.recipes = null;
		return recipes;
	}
	
	protected void smelting(ItemStack in, ItemStack out)
	{
		smelting(in, out, 0);
	}
	
	protected void oredict(String od, Item i)
	{
		OreDictionary.registerOre(od, i);
	}
	
	protected void oredict(String od, Block b)
	{
		OreDictionary.registerOre(od, b);
	}
	
	protected void oredict(String od, ItemStack s)
	{
		OreDictionary.registerOre(od, s);
	}
	
	protected void smelting(Item in, ItemStack out, float xp)
	{
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(in), out, xp);
	}
	
	protected void smelting(ItemStack in, ItemStack out, float xp)
	{
		FurnaceRecipes.instance().addSmeltingRecipe(in, out, xp);
	}
	
	protected IRecipe shaped(ItemStack out, Object... recipeComponents)
	{
		return recipe(SimpleRegistration.parseShapedRecipe(out, recipeComponents));
	}
	
	protected IRecipe shaped(Item out, Object... recipeComponents)
	{
		return recipe(SimpleRegistration.parseShapedRecipe(new ItemStack(out), recipeComponents));
	}
	
	protected IRecipe shaped(Block out, Object... recipeComponents)
	{
		return recipe(SimpleRegistration.parseShapedRecipe(new ItemStack(out), recipeComponents));
	}
	
	protected IRecipe shapeless(ItemStack out, Object... recipeComponents)
	{
		return recipe(SimpleRegistration.parseShapelessRecipe(out, recipeComponents));
	}
	
	protected IRecipe shapeless(Item out, Object... recipeComponents)
	{
		return recipe(SimpleRegistration.parseShapelessRecipe(new ItemStack(out), recipeComponents));
	}
	
	protected IRecipe shapeless(Block out, Object... recipeComponents)
	{
		return recipe(SimpleRegistration.parseShapelessRecipe(new ItemStack(out), recipeComponents));
	}
	
	/**
	 * Gets the modid of the registry
	 * 
	 * @return modid of this registry.
	 */
	protected String getMod()
	{
		RegisterRecipes a = getClass().getAnnotation(RegisterRecipes.class);
		return a != null ? a.modid() : "unknown";
	}
	
	public ModContainer getOwner()
	{
		return Loader.instance().getIndexedModList().get(getMod());
	}
	
	protected IRecipe recipe(IRecipe recipe)
	{
		if(recipe.getRegistryName() == null)
			recipe = recipe.setRegistryName(new ResourceLocation("hammercore", getMod() + "_" + getClass().getSimpleName() + "." + recipes.size()));
		recipes.add(recipe);
		return recipe;
	}
}