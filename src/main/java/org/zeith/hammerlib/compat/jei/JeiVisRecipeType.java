package org.zeith.hammerlib.compat.jei;

import mezz.jei.api.recipe.RecipeType;

public class JeiVisRecipeType
{
	public static RecipeType<?> getJEIType(net.minecraft.world.item.crafting.RecipeType<?> type)
	{
		return JeiHammerLib.MC2JEI.get(type);
	}
}