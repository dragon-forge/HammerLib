package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

public class SmokingRecipeBuilder
		extends AbstractCookingRecipeBuilder<SmokingRecipeBuilder>
{
	public SmokingRecipeBuilder(RegisterRecipesEvent event)
	{
		super(event);
		cookTime = 100;
	}

	@Override
	protected Recipe<?> generateRecipe()
	{
		return new SmokingRecipe(getIdentifier(), group, input, result, xp, cookTime);
	}
}