package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipe;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

public class SmeltingRecipeBuilder
		extends AbstractCookingRecipeBuilder<SmeltingRecipeBuilder>
{
	public SmeltingRecipeBuilder(RegisterRecipesEvent event)
	{
		super(event);
	}

	@Override
	protected IRecipe<?> generateRecipe()
	{
		return new FurnaceRecipe(getIdentifier(), group, input, result, xp, cookTime);
	}
}