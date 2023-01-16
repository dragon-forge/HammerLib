package org.zeith.hammerlib.abstractions.recipes.layout;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

public interface IIngredientReceiver<THIS extends IIngredientReceiver<THIS>>
{
	THIS addIngredientsUnsafe(List<?> ingredients);
	
	THIS addIngredients(Ingredient ingredient);
	
	THIS addItemStacks(List<ItemStack> itemStacks);
	
	THIS addItemStack(ItemStack itemStack);
	
	THIS addFluidStack(Fluid fluid, long amount);
	
	THIS addFluidStack(Fluid fluid, long amount, CompoundTag data);
}