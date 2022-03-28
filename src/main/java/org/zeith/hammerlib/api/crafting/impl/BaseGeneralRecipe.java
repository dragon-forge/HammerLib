package org.zeith.hammerlib.api.crafting.impl;

import net.minecraft.core.NonNullList;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.zeith.hammerlib.api.crafting.IBaseIngredient;
import org.zeith.hammerlib.api.crafting.ICraftingResult;
import org.zeith.hammerlib.api.crafting.IGeneralRecipe;
import org.zeith.hammerlib.api.energy.EnergyUnit;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.util.charging.fe.FECharge;

/**
 * A simple implementation for {@link IGeneralRecipe} featuring inputs to output
 * feature. Use {@link Builder}
 */
public class BaseGeneralRecipe
		implements IGeneralRecipe
{
	public final ICraftingResult<?> output;
	public final NonNullList<IBaseIngredient> ingredients;

	public BaseGeneralRecipe(ICraftingResult<?> output, NonNullList<IBaseIngredient> ingredients)
	{
		this.output = output;
		this.ingredients = ingredients;
	}

	@Override
	public NonNullList<IBaseIngredient> getIngredients()
	{
		return ingredients;
	}

	@Override
	public ICraftingResult<?> getResult()
	{
		return output;
	}

	public static Builder builder()
	{
		return new Builder();
	}

	/**
	 * A Builder class for {@link BaseGeneralRecipe}. allows to register inputs,
	 * fluids, energy and set output for a recipe, then build it.
	 */
	public static class Builder
	{
		private final NonNullList<Ingredient> inputItems = NonNullList.create();
		private final NonNullList<FluidStack> inputFluid = NonNullList.create();
		private ICraftingResult<?> output;
		private double FE;

		/**
		 * Adds an ingredient to input list.
		 *
		 * @param ing the ingredient to be added
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
		 * @param tag the entry to be added as ingredient
		 * @return this builder, for convenience.
		 */
		public Builder addInput(TagKey<Item> tag)
		{
			inputItems.add(RecipeHelper.fromTag(tag));
			return this;
		}

		/**
		 * Adds an fluid to input list.
		 *
		 * @param stack the fluid to be added to fluid list
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
		 * @param amount the amount of energy
		 * @param unit   the unit of energy
		 * @return this builder, for convenience.
		 */
		public Builder setEnergy(double amount, EnergyUnit unit)
		{
			FE = unit.getInFE(amount);
			return this;
		}

		/**
		 * Adds energy to the builder, in desired units.
		 *
		 * @param amount the amount of energy
		 * @param unit   the unit of energy
		 * @return this builder, for convenience.
		 */
		public Builder addEnergy(double amount, EnergyUnit unit)
		{
			FE += unit.getInFE(amount);
			return this;
		}

		/**
		 * Sets recipe output.
		 *
		 * @param stack the output stack
		 * @return this builder, for convenience.
		 */
		public Builder withOutput(ItemStack stack)
		{
			output = new ItemStackResult(stack);
			return this;
		}

		/**
		 * Sets recipe output.
		 *
		 * @param stack the output stack
		 * @return this builder, for convenience.
		 */
		public Builder withOutput(FluidStack stack)
		{
			output = new FluidStackResult(stack);
			return this;
		}

		/**
		 * Sets recipe output.
		 *
		 * @param FE the output energy
		 * @return this builder, for convenience.
		 */
		public Builder withOutput(int FE)
		{
			output = new ForgeEnergyResult(new FECharge(FE));
			return this;
		}

		/**
		 * Sets recipe output.
		 *
		 * @param stack the output stack
		 * @return this builder, for convenience.
		 */
		public Builder withOutput(ICraftingResult<?> stack)
		{
			output = stack;
			return this;
		}

		/**
		 * Retrieves the energy amount needed to perform the recipe in units.
		 *
		 * @param unit the unit the we retrieve energy in.
		 * @return the amount of energy
		 */
		public double getEnergy(EnergyUnit unit)
		{
			return unit.getFromFE(FE);
		}

		/**
		 * Builds the recipe
		 *
		 * @return the newly built {@link BaseGeneralRecipe}
		 */
		public BaseGeneralRecipe build()
		{
			NonNullList<IBaseIngredient> ings = NonNullList.create();
			if(FE > 0) ings.add(new EnergyIngredient(FE, EnergyUnit.RF));
			for(FluidStack stack : inputFluid) ings.add(new FluidStackIngredient(stack));
			for(Ingredient ing : inputItems) ings.add(new MCIngredient(ing));
			return new BaseGeneralRecipe(output, ings);
		}
	}
}