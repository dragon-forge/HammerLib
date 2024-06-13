package org.zeith.hammerlib.api.recipes;

import net.minecraft.world.item.crafting.RecipeInput;

public abstract class BaseCustomRecipe<R extends BaseCustomRecipe<R>>
		extends BaseRecipe<R, RecipeInput>
{
	public BaseCustomRecipe(String group)
	{
		super(group);
	}
}