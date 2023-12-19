package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.world.item.crafting.*;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

public class CampfireRecipeBuilder
		extends AbstractCookingRecipeBuilder<CampfireRecipeBuilder>
{
	public CampfireRecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		super(event);
		cookTime = 100;
	}
	
	@Override
	protected Recipe<?> generateRecipe()
	{
		return new CampfireCookingRecipe(group, category, input, result, xp, cookTime);
	}
}