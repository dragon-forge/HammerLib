package com.zeitheron.hammercore.bookAPI.fancy;

import java.util.ArrayList;
import java.util.List;

import com.zeitheron.hammercore.utils.InterItemStack;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A page in a {@link ManualEntry}.
 */
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
	
	/**
	 * Constructs a page with custom {@link PageType} and recipe. This will use
	 * custom rendering for the page.
	 * 
	 * @param recipe
	 *            The custom recipe to be displayed.
	 * @param page
	 *            The page that will handle rendering for this recipe(s).
	 */
	public ManualPage(Object recipe, PageType page)
	{
		type = page;
		this.recipe = recipe;
	}
	
	/**
	 * Constructs a page with simple text. This value will be translated later.
	 * 
	 * @param text
	 *            The I18n key. Use as template:
	 *            "MOD_ID.manual_desc.ENTRY_ID.PAGE_NUMBER".
	 */
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
	
	/**
	 * Constructs a new page with text, and an output association.
	 * 
	 * @param text
	 *            The I18n key. Use as template:
	 *            "MOD_ID.manual_desc.ENTRY_ID.PAGE_NUMBER".
	 * @param output
	 *            The associated output to this page.
	 */
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
	
	/**
	 * WIP feature.<br>
	 * Allows to render text in galactic font instead, if player doesn't have
	 * required knowledge.
	 * 
	 * @param research
	 *            The knowledge ID to be required to read this page.
	 * @param text
	 *            The I18n key. Use as template:
	 *            "MOD_ID.manual_desc.ENTRY_ID.PAGE_NUMBER".
	 */
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
	
	/**
	 * Constructs a page with multiblock structure.
	 * 
	 * @param recipe
	 *            The list that represents the multiblock structure.
	 */
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
	
	/**
	 * Constructs a new page with smelting recipe.
	 * 
	 * @param input
	 *            The smelting input.
	 */
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
	
	/**
	 * Constructs a new page that will have an image and a caption.
	 * 
	 * @param image
	 *            The image to be rendered.
	 * @param caption
	 *            The caption after the image.
	 */
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
	
	/**
	 * Allows for creating a page with {@link PageType#NORMAL_CRAFTING} and
	 * {@link PageType#SMELTING}.
	 * 
	 * @param type
	 *            The page type. May be 1 of those above.
	 * @param item
	 *            The recipe output, if crafting. The smelting input, if
	 *            smelting.
	 */
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
	
	/**
	 * Adds the items that are outputs on this page.
	 * 
	 * @param associations
	 *            The associated output items
	 * @return This page, for convenience
	 */
	public ManualPage addClickthroughAssociation(ItemStack... associations)
	{
		for(ItemStack a : associations)
			allOutputs.add(a);
		return this;
	}
	
	/**
	 * @return the localized text (description) on this page.
	 */
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
		
		/**
		 * Constructs a new {@link PageType}.
		 * 
		 * @param var1
		 *            the UPPERCASE id of the page type. Internal use only
		 */
		private PageType(String var1)
		{
			this(var1, null);
		}
		
		/**
		 * Constructs a new {@link PageType}.
		 * 
		 * @param var1
		 *            the UPPERCASE id of the page type.
		 * @param renderClass
		 *            the class which will be used to render. Example:
		 *            "com.author.mod.client.render.manual.CustomManualRenderer".
		 *            The passed class must also implement
		 *            {@link IManualPageRender} for rendering to work.
		 */
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
		public IManualPageRender getRender()
		{
			if(renderClass == null || renderClass.isEmpty())
				return null;
			
			if(render == null)
			{
				try
				{
					render = Class.forName(renderClass).newInstance();
				} catch(Throwable err)
				{
					System.err.println("Unable to construct IManualPageRender");
					err.printStackTrace();
					render = new Object();
				}
			}
			
			return (IManualPageRender) render;
		}
	}
}