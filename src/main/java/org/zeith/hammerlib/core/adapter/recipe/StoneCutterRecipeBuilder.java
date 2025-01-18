package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;


public class StoneCutterRecipeBuilder
		extends SingleItemRecipeBuilder<StoneCutterRecipeBuilder>
{
	public StoneCutterRecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		super(event);
	}
	
	@Override
	protected Recipe<?> createRecipe()
	{
		return new StonecutterRecipe(group, input, result);
	}
}