package com.zeitheron.hammercore.api.crafting.impl;

import java.util.ArrayList;
import java.util.List;

import com.zeitheron.hammercore.api.EnergyUnit;
import com.zeitheron.hammercore.api.crafting.IBaseIngredient;
import com.zeitheron.hammercore.api.crafting.IGeneralRecipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreIngredient;

public class BaseGeneralRecipe implements IGeneralRecipe
{
	public final ItemStack output;
	public final NonNullList<IBaseIngredient> ingredients;
	
	public BaseGeneralRecipe(ItemStack output, IBaseIngredient... ingredients)
	{
		this.output = output;
		
		this.ingredients = NonNullList.withSize(ingredients.length, new EnergyIngredient(0, EnergyUnit.RF));
		for(int i = 0; i < ingredients.length; ++i)
			this.ingredients.set(i, ingredients[i]);
	}
	
	@Override
	public NonNullList<IBaseIngredient> getIngredients()
	{
		return ingredients;
	}
	
	@Override
	public ItemStack getRecipeOutputOriginal()
	{
		return output;
	}
	
	public static class Builder
	{
		private NonNullList<Ingredient> inputItems = NonNullList.create();
		private NonNullList<FluidStack> inputFluid = NonNullList.create();
		private NonNullList<ItemStack> output = NonNullList.withSize(1, ItemStack.EMPTY);
		private double RF;
		
		public Builder addInput(Ingredient ing)
		{
			inputItems.add(ing);
			return this;
		}
		
		public Builder addInput(String oredict)
		{
			inputItems.add(new OreIngredient(oredict));
			return this;
		}
		
		public Builder addInput(FluidStack stack)
		{
			inputFluid.add(stack);
			return this;
		}
		
		public Builder setEnergy(double amount, EnergyUnit unit)
		{
			RF = unit.getInRF(amount);
			return this;
		}
		
		public Builder addEnergy(double amount, EnergyUnit unit)
		{
			RF += unit.getInRF(amount);
			return this;
		}
		
		public Builder withOutput(ItemStack stack)
		{
			output.set(0, stack);
			return this;
		}
		
		public double getEnergy(EnergyUnit unit)
		{
			return unit.getFromRF(RF);
		}
		
		public BaseGeneralRecipe build()
		{
			List<IBaseIngredient> ings = new ArrayList<>();
			if(RF > 0)
				ings.add(new EnergyIngredient(RF, EnergyUnit.RF));
			for(FluidStack stack : inputFluid)
				ings.add(new FluidStackIngredient(stack));
			for(Ingredient ing : inputItems)
				ings.add(new MCIngredient(ing));
			return new BaseGeneralRecipe(output.get(0), ings.toArray(new IBaseIngredient[ings.size()]));
		}
	}
}