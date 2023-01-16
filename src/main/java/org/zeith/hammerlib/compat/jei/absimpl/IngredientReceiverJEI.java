package org.zeith.hammerlib.compat.jei.absimpl;

import mezz.jei.api.gui.builder.IIngredientAcceptor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import org.zeith.hammerlib.abstractions.recipes.layout.IIngredientReceiver;

import java.util.List;

public class IngredientReceiverJEI<THIS extends IngredientReceiverJEI<THIS>>
		implements IIngredientReceiver<THIS>
{
	final IIngredientAcceptor<?> slot;
	
	public IngredientReceiverJEI(IIngredientAcceptor<?> acceptor)
	{
		this.slot = acceptor;
	}
	
	@Override
	public THIS addIngredientsUnsafe(List<?> ingredients)
	{
		slot.addIngredientsUnsafe(ingredients);
		return (THIS) this;
	}
	
	@Override
	public THIS addIngredients(Ingredient ingredient)
	{
		slot.addIngredients(ingredient);
		return (THIS) this;
	}
	
	@Override
	public THIS addItemStacks(List<ItemStack> itemStacks)
	{
		slot.addItemStacks(itemStacks);
		return (THIS) this;
	}
	
	@Override
	public THIS addItemStack(ItemStack itemStack)
	{
		slot.addItemStack(itemStack);
		return (THIS) this;
	}
	
	@Override
	public THIS addFluidStack(Fluid fluid, long amount)
	{
		slot.addFluidStack(fluid, amount);
		return (THIS) this;
	}
	
	@Override
	public THIS addFluidStack(Fluid fluid, long amount, CompoundTag data)
	{
		slot.addFluidStack(fluid, amount, data);
		return (THIS) this;
	}
}