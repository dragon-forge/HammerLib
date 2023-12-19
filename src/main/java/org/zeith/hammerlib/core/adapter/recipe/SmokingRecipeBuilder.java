package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

public class SmokingRecipeBuilder
		extends AbstractCookingRecipeBuilder<SmokingRecipeBuilder>
{
	public SmokingRecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		super(event);
		cookTime = 100;
	}
	
	@Override
	protected Recipe<?> generateRecipe()
	{
		return new SmokingRecipe( group, category, input, result, xp, cookTime);
	}
}