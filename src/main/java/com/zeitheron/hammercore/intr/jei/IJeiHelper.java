package com.zeitheron.hammercore.intr.jei;

import javax.annotation.Nonnull;

import net.minecraftforge.fluids.FluidStack;

public interface IJeiHelper
{
	void addJEI(Object recipe);
	
	void removeJEI(Object recipe);
	
	<T> void showRecipes(T ingredient);
	
	<T> void showUses(T ingredient);
	
	Object getKeybind_showRecipes();
	
	Object getKeybind_showUses();
	
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
		public Object getKeybind_showRecipes()
		{
			return null;
		}
		
		@Override
		public Object getKeybind_showUses()
		{
			return null;
		}
	}
}