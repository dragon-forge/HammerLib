package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

public class SmeltingRecipeBuilder
		extends AbstractCookingRecipeBuilder<SmeltingRecipeBuilder>
{
	public SmeltingRecipeBuilder(RegisterRecipesEvent event)
	{
		super(event);
	}

	@Override
	protected Recipe<?> generateRecipe()
	{
		return new SmeltingRecipe(getIdentifier(), group, input, result, xp, cookTime);
	}
}