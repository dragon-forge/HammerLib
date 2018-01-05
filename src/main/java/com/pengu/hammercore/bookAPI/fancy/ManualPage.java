package com.pengu.hammercore.bookAPI.fancy;

import java.util.ArrayList;
import java.util.List;

import com.pengu.hammercore.common.InterItemStack;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ManualPage
{
	public ManualPage.PageType type = ManualPage.PageType.TEXT;
	public String text;
	public String research;
	public ResourceLocation image;
	private Object recipe;
	public ItemStack recipeOutput;
	public final List<ItemStack> allOutputs = new ArrayList<>();
	
	public static List<IRecipe> findRecipesFor(ItemStack out)
	{
		List<IRecipe> recipes = new ArrayList<>();
		
		CraftingManager.REGISTRY.forEach(recipe ->
		{
			if(recipe.getRecipeOutput().isItemEqual(out))
				recipes.add(recipe);
		});
		
		return recipes;
	}
	
	public Object getRecipe()
	{
		if(type == PageType.NORMAL_CRAFTING)
			if(recipe instanceof ItemStack[])
			{
				List<IRecipe> rs = new ArrayList<>();
				for(ItemStack r : (ItemStack[]) recipe)
					rs.addAll(findRecipesFor(r));
				return rs.toArray(new IRecipe[0]);
			} else if(recipe instanceof ItemStack)
				return findRecipesFor((ItemStack) recipe).toArray(new IRecipe[0]);
			
		if(type == PageType.SMELTING && InterItemStack.isStackNull(recipeOutput) && recipe instanceof ItemStack)
			recipeOutput = FurnaceRecipes.instance().getSmeltingResult((ItemStack) recipe);
		
		return recipe;
	}
	
	public ManualPage setRecipe(Object recipe)
	{
		this.recipe = recipe;
		return this;
	}
	
	/** For adding custom recipe support */
	public ManualPage(Object recipe, PageType page)
	{
		type = page;
		this.recipe = recipe;
	}
	
	public ManualPage(String text)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = ManualPage.PageType.TEXT;
		this.text = text;
	}
	
	public ManualPage(String text, ItemStack output)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipe = null;
		this.recipeOutput = output.copy();
		allOutputs.add(output.copy());
		this.type = ManualPage.PageType.TEXT;
		this.text = text;
	}
	
	public ManualPage(String research, String text)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = ManualPage.PageType.TEXT_CONCEALED;
		this.research = research;
		this.text = text;
	}
	
	public ManualPage(List recipe)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = ManualPage.PageType.COMPOUND_CRAFTING;
		this.recipe = recipe;
	}
	
	public ManualPage(ItemStack input)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = ManualPage.PageType.SMELTING;
		this.recipe = input;
		this.recipeOutput = FurnaceRecipes.instance().getSmeltingResult(input).copy();
	}
	
	public ManualPage(ResourceLocation image, String caption)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = ManualPage.PageType.IMAGE;
		this.image = image;
		this.text = caption;
	}
	
	public ManualPage(PageType type, ItemStack item)
	{
		if(type == PageType.NORMAL_CRAFTING)
		{
			this.text = null;
			this.research = null;
			this.image = null;
			this.type = type;
			this.recipe = item;
			allOutputs.add(item.copy());
			this.recipeOutput = item;
		} else if(type == PageType.SMELTING)
		{
			this.text = null;
			this.research = null;
			this.image = null;
			this.recipe = null;
			this.recipeOutput = null;
			this.type = type;
			this.recipe = item;
			this.recipeOutput = FurnaceRecipes.instance().getSmeltingResult(item).copy();
			allOutputs.add(recipeOutput.copy());
		}
	}
	
	public ManualPage addClickthroughAssociation(ItemStack... associations)
	{
		for(ItemStack a : associations)
			allOutputs.add(a);
		return this;
	}
	
	public String getTranslatedText()
	{
		String ret = "";
		
		if(this.text != null)
		{
			ret = I18n.format(this.text);
			if(ret.isEmpty())
				ret = this.text;
		}
		
		return ret;
	}
	
	public static class PageType
	{
		public static final PageType //
		TEXT = new PageType("TEXT"), //
		        TEXT_CONCEALED = new PageType("TEXT_CONCEALED"), //
		        IMAGE = new PageType("IMAGE"), //
		        NORMAL_CRAFTING = new PageType("NORMAL_CRAFTING"), //
		        COMPOUND_CRAFTING = new PageType("COMPOUND_CRAFTING"), //
		        SMELTING = new PageType("SMELTING");
		
		private final String v1;
		private Object render;
		private final String renderClass;
		
		public PageType(String var1)
		{
			this(var1, null);
		}
		
		public PageType(String var1, String renderClass)
		{
			v1 = var1;
			this.renderClass = renderClass;
		}
		
		public String getV1()
		{
			return v1;
		}
		
		@SideOnly(Side.CLIENT)
		public iManualPageRender getRender()
		{
			if(renderClass == null || renderClass.isEmpty())
				return null;
			
			if(render == null)
			{
				try
				{
					render = (iManualPageRender) Class.forName(renderClass).newInstance();
				} catch(Throwable err)
				{
					System.err.println("Unable to construct iRenderExtension");
					err.printStackTrace();
				}
			}
			
			return (iManualPageRender) render;
		}
	}
}