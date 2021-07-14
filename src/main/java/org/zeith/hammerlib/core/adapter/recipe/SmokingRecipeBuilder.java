package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.SmokingRecipe;
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
	protected IRecipe<?> generateRecipe()
	{
		return new SmokingRecipe(getIdentifier(), group, input, result, xp, cookTime);
	}
}