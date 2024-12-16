package org.zeith.hammerlib.compat.jei.absimpl;

import mezz.jei.api.gui.builder.IIngredientAcceptor;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.material.Fluid;
import org.zeith.hammerlib.abstractions.recipes.layout.IIngredientReceiver;
import org.zeith.hammerlib.api.recipes.IngredientWithCount;

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
	public THIS addIngredient(Ingredient ingredient)
	{
		slot.addIngredients(ingredient);
		return (THIS) this;
	}
	
	@Override
	public THIS addIngredient(IngredientWithCount ingredient)
	{
		var level = Minecraft.getInstance().level;
		ContextMap context;
		if(level != null) context = SlotDisplayContext.fromLevel(level);
		else context = new ContextMap.Builder().create(SlotDisplayContext.CONTEXT);
		
		slot.addItemStacks(ingredient.input()
				.display()
				.resolveForStacks(context)
				.stream()
				.peek(i -> i.setCount(ingredient.count()))
				.toList()
		);
		
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
	public THIS addFluidStack(Fluid fluid, long amount, DataComponentPatch data)
	{
		slot.addFluidStack(fluid, amount, data);
		return (THIS) this;
	}
}