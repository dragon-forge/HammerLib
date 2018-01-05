package com.pengu.hammercore.intr.jei;

public interface iJeiRecipeModifier
{
	void addJEI(Object recipe);
	
	void removeJEI(Object recipe);
	
	public static class Instance
	{
		public static iJeiRecipeModifier JEIModifier;
	}
}