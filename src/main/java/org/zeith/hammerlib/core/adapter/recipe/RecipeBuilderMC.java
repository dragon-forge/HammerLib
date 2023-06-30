package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.world.item.crafting.Recipe;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

public abstract class RecipeBuilderMC<R extends RecipeBuilderMC<R>>
		extends RecipeBuilder<R, Recipe<?>>
{
	public RecipeBuilderMC(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		super(event);
	}
}
