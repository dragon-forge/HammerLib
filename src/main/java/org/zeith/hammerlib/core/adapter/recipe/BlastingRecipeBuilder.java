package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

public class BlastingRecipeBuilder
		extends AbstractCookingRecipeBuilder<BlastingRecipeBuilder>
{
	public BlastingRecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		super(event);
		cookTime = 100;
	}
	
	@Override
	protected Recipe<?> generateRecipe()
	{
		return new BlastingRecipe(getIdentifier(), group, category, input, result, xp, cookTime);
	}
}