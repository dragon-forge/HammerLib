package org.zeith.hammerlib.abstractions.recipes.layout;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import org.zeith.hammerlib.api.recipes.IngredientWithCount;

import java.util.List;

public interface IIngredientReceiver<THIS extends IIngredientReceiver<THIS>>
{
	THIS addIngredientsUnsafe(List<?> ingredients);
	
	THIS addIngredient(Ingredient ingredient);
	
	THIS addIngredient(IngredientWithCount ingredient);
	
	THIS addItemStacks(List<ItemStack> itemStacks);
	
	THIS addItemStack(ItemStack itemStack);
	
	THIS addFluidStack(Fluid fluid, long amount);
	
	THIS addFluidStack(Fluid fluid, long amount, DataComponentPatch data);
}