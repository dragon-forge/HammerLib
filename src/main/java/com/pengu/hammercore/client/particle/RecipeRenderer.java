package com.pengu.hammercore.client.particle;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeRenderer
{
	public static void selectAndRender(IRecipe recipe)
	{
		if(recipe instanceof ShapedOreRecipe)
			render((ShapedOreRecipe) recipe);
		if(recipe instanceof ShapelessOreRecipe)
			render((ShapelessOreRecipe) recipe);
		if(recipe instanceof ShapedRecipes)
			render((ShapedRecipes) recipe);
		if(recipe instanceof ShapelessRecipes)
			render((ShapelessRecipes) recipe);
	}
	
	public static void render(ShapedOreRecipe recipe)
	{
		renderPattern((Ingredient[]) recipe.getIngredients().toArray());
	}
	
	public static void render(ShapedRecipes recipe)
	{
		renderPattern((Ingredient[]) recipe.recipeItems.toArray());
	}
	
	public static void render(ShapelessOreRecipe recipe)
	{
		renderPattern((Ingredient[]) recipe.getIngredients().toArray());
	}
	
	public static void render(ShapelessRecipes recipe)
	{
		renderPattern((Ingredient[]) recipe.recipeItems.toArray());
	}
	
	public static void renderPattern(Ingredient[] recipe)
	{
		GL11.glPushMatrix();
		
		if(recipe.length <= 4)
			for(int i = 0; i < Math.min(recipe.length, 4); ++i)
			{
				int x = i % 2;
				int y = i / 2;
				Ingredient o = recipe[i];
				ItemStack[] ss = o.getMatchingStacks();
				
				GL11.glPushMatrix();
				GlStateManager.disableLighting();
				RenderHelper.enableGUIStandardItemLighting();
				if(ss != null)
					Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(ss[getInRange(500, 0, ss.length)], x * 18, y * 18);
				GL11.glPopMatrix();
			}
		
		if(recipe.length >= 9)
			for(int i = 0; i < Math.min(recipe.length, 9); ++i)
			{
				int x = i % 3;
				int y = i / 3;
				Object o = recipe[i];
				
				GL11.glPushMatrix();
				GlStateManager.disableLighting();
				RenderHelper.enableGUIStandardItemLighting();
				if(o instanceof ItemStack)
					Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI((ItemStack) o, x * 18, y * 18);
				else if(o instanceof ItemStack[])
				{
					ItemStack[] ss = (ItemStack[]) o;
					Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(ss[getInRange(500, 0, ss.length)], x * 18, y * 18);
				} else if(o instanceof List && ((List) o).size() > 0 && ((List) o).get(0) instanceof ItemStack)
				{
					List<ItemStack> ss = (List<ItemStack>) o;
					Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(ss.get(getInRange(500, 0, ss.size())), x * 18, y * 18);
				}
				GL11.glPopMatrix();
			}
		
		GL11.glPopMatrix();
	}
	
	public static int getInRange(int delayMillis, int min, int max)
	{
		int sys = (int) (System.currentTimeMillis() % (max * delayMillis)) / delayMillis;
		return sys;
	}
}