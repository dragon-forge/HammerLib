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

/**
 * A simple implementation for {@link IGeneralRecipe} featuring inputs -> output
 * feature. Use {@link BaseGeneralRecipe.Builder}
 */
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
	
	/**
	 * A Builder class for {@link BaseGeneralRecipe}. allows to register inputs,
	 * fluids, energy and set output for a recipe, then build it.
	 */
	public static class Builder
	{
		private NonNullList<Ingredient> inputItems = NonNullList.create();
		private NonNullList<FluidStack> inputFluid = NonNullList.create();
		private NonNullList<ItemStack> output = NonNullList.withSize(1, ItemStack.EMPTY);
		private double RF;
		
		/**
		 * Adds an ingredient to input list.
		 * 
		 * @param ing
		 *            the ingredient to be added
		 * @return this builder, for convenience.
		 */
		public Builder addInput(Ingredient ing)
		{
			inputItems.add(ing);
			return this;
		}
		
		/**
		 * Adds an Ore Dictionary entry to input list.
		 * 
		 * @param oredict
		 *            the entry to be added as ingredient
		 * @return this builder, for convenience.
		 */
		public Builder addInput(String oredict)
		{
			inputItems.add(new OreIngredient(oredict));
			return this;
		}
		
		/**
		 * Adds an fluid to input list.
		 * 
		 * @param stack
		 *            the fluid to be added to fluid list
		 * @return this builder, for convenience.
		 */
		public Builder addInput(FluidStack stack)
		{
			inputFluid.add(stack);
			return this;
		}
		
		/**
		 * Sets energy value in desired unit.
		 * 
		 * @param amount
		 *            the amount of energy
		 * @param unit
		 *            the unit of energy
		 * @return this builder, for convenience.
		 */
		public Builder setEnergy(double amount, EnergyUnit unit)
		{
			RF = unit.getInRF(amount);
			return this;
		}
		
		/**
		 * Adds energy to the builder, in desired units.
		 * 
		 * @param amount
		 *            the amount of energy
		 * @param unit
		 *            the unit of energy
		 * @return this builder, for convenience.
		 */
		public Builder addEnergy(double amount, EnergyUnit unit)
		{
			RF += unit.getInRF(amount);
			return this;
		}
		
		/**
		 * Sets recipe output.
		 * 
		 * @param stack
		 *            the output stack
		 * @return this builder, for convenience.
		 */
		public Builder withOutput(ItemStack stack)
		{
			output.set(0, stack);
			return this;
		}
		
		/**
		 * Retrieves the energy amount needed to perform the recipe in units.
		 * 
		 * @param unit
		 *            the unit the we retrieve energy in.
		 * @return the amount of energy
		 */
		public double getEnergy(EnergyUnit unit)
		{
			return unit.getFromRF(RF);
		}
		
		/**
		 * Builds the recipe
		 * 
		 * @return the newly built {@link BaseGeneralRecipe}
		 */
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