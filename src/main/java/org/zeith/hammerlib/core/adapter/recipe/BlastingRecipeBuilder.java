package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

public class BlastingRecipeBuilder
		extends AbstractCookingRecipeBuilder<BlastingRecipeBuilder>
{
	public BlastingRecipeBuilder(RegisterRecipesEvent event)
	{
		super(event);
		cookTime = 100;
	}

	@Override
	protected Recipe<?> generateRecipe()
	{
		return new BlastingRecipe(getIdentifier(), group, input, result, xp, cookTime);
	}
}