package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

public class CampfireRecipeBuilder
		extends AbstractCookingRecipeBuilder<CampfireRecipeBuilder>
{
	public CampfireRecipeBuilder(RegisterRecipesEvent event)
	{
		super(event);
		cookTime = 100;
	}

	@Override
	protected IRecipe<?> generateRecipe()
	{
		return new CampfireCookingRecipe(getIdentifier(), group, input, result, xp, cookTime);
	}
}