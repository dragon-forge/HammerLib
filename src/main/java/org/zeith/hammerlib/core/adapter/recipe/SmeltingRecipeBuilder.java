package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

public class SmeltingRecipeBuilder
		extends AbstractCookingRecipeBuilder<SmeltingRecipeBuilder>
{
	public SmeltingRecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		super(event);
	}

	@Override
	protected Recipe<?> generateRecipe()
	{
		return new SmeltingRecipe(getIdentifier(), group, input, result, xp, cookTime);
	}
}