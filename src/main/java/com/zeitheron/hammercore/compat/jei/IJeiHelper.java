package com.zeitheron.hammercore.compat.jei;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IJeiHelper
{
	static IJeiHelper instance()
	{
		return Instance.getJEIModifier();
	}
	
	void addJEI(Object recipe);
	
	void removeJEI(Object recipe);
	
	<T> void showRecipes(T ingredient);
	
	<T> void showUses(T ingredient);
	
	void showCategories(String... uids);
	
	Object getKeybind_showRecipes();
	
	Object getKeybind_showUses();
	
	ItemStack getSlotUnderMouseInJEI();
	
	default void showFluidRecipes(FluidStack fluid)
	{
		showRecipes(fluid);
	}
	
	default void showFluidUses(FluidStack fluid)
	{
		showUses(fluid);
	}
	
	public static class Instance implements IJeiHelper
	{
		@Nonnull
		static IJeiHelper JEIModifier = new Instance();
		
		public static IJeiHelper getJEIModifier()
		{
			return JEIModifier;
		}
		
		@Override
		public void addJEI(Object recipe)
		{
		}
		
		@Override
		public void removeJEI(Object recipe)
		{
		}
		
		@Override
		public <T> void showRecipes(T ingredient)
		{
		}
		
		@Override
		public <T> void showUses(T ingredient)
		{
		}
		
		@Override
		public void showCategories(String... uid)
		{
		}
		
		@Override
		public Object getKeybind_showRecipes()
		{
			return null;
		}
		
		@Override
		public Object getKeybind_showUses()
		{
			return null;
		}
		
		@Override
		public ItemStack getSlotUnderMouseInJEI()
		{
			return ItemStack.EMPTY;
		}
	}
}