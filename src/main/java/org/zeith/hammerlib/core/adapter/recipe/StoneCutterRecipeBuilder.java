package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.world.item.crafting.StonecutterRecipe;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

public class StoneCutterRecipeBuilder
		extends SingleItemRecipeBuilder<StoneCutterRecipeBuilder>
{
	public StoneCutterRecipeBuilder(RegisterRecipesEvent event)
	{
		super(event);
	}

	@Override
	public void register()
	{
		validate();
		event.add(new StonecutterRecipe(getIdentifier(), group, input, result));
	}
}