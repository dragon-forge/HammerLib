package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.world.item.crafting.*;
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
		return new SmeltingRecipe(group, category, input, result, xp, cookTime);
	}
}