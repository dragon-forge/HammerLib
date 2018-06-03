package com.zeitheron.hammercore.api.recipe;

import java.util.Map;

import com.pengu.hammercore.common.utils.XPUtil;
import com.pengu.hammercore.recipeAPI.helper.ShapelessRecipeBuilder;
import com.zeitheron.hammercore.annotations.WIP;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

@WIP
public class ShapelessXPRecipeBuilder extends ShapelessRecipeBuilder
{
	@Override
	protected IRecipe bake(NonNullList<Ingredient> ings, NonNullList<Ingredient> stay, Map<Ingredient, ItemStack> returns)
	{
		return new BakedShapelessRecipe(this, output, ings, stay, returns);
	}
	
	public static class XPCost
	{
		final XPType type;
		final Number cost;
		
		public XPCost(XPType type, int value)
		{
			this.type = type;
			this.cost = value;
		}
		
		public XPCost(XPType type, float value)
		{
			this.type = type;
			this.cost = value;
		}
		
		public XPType getType()
		{
			return type;
		}
		
		public Number getCost()
		{
			return cost;
		}
		
		public boolean has(EntityPlayer crafter)
		{
			if(type == XPType.CURRENT)
				return crafter.experience >= cost.floatValue();
			if(type == XPType.LEVEL)
				return crafter.experienceLevel >= cost.intValue();
			if(type == XPType.POINTS)
				return XPUtil.getXPTotal(crafter) >= cost.intValue();
			return false;
		}
	}
	
	public static enum XPType
	{
		CURRENT, LEVEL, POINTS;
	}
	
	protected static class BakedXPShapelessRecipe extends BakedShapelessRecipe
	{
		
		public BakedXPShapelessRecipe(ShapelessRecipeBuilder builder, ItemStack output, NonNullList<Ingredient> ingredients, NonNullList<Ingredient> stay, Map<Ingredient, ItemStack> returns)
		{
			super(builder, output, ingredients, stay, returns);
		}
		
		@Override
		public boolean matches(InventoryCrafting inv, World worldIn)
		{
			return super.matches(inv, worldIn);
		}
		
		@Override
		public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
		{
			return super.getRemainingItems(inv);
		}
		
	}
}