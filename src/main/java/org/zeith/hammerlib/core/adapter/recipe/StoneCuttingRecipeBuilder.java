package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.item.crafting.StonecuttingRecipe;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

public class StoneCuttingRecipeBuilder
		extends SingleItemRecipeBuilder<StoneCuttingRecipeBuilder>
{
	public StoneCuttingRecipeBuilder(RegisterRecipesEvent event)
	{
		super(event);
	}

	@Override
	public void register()
	{
		validate();
		event.add(new StonecuttingRecipe(getIdentifier(), group, input, result));
	}
}