package org.zeith.hammerlib.core.test;

import org.zeith.hammerlib.api.recipes.RecipeBuilderExtension;
import org.zeith.hammerlib.core.test.machine.RecipeTestMachine;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

@RecipeBuilderExtension.RegisterExt
public class HammerLibRecipeExtension
		extends RecipeBuilderExtension
{
	public HammerLibRecipeExtension(RegisterRecipesEvent event)
	{
		super(event);
	}
	
	public RecipeTestMachine.TestMachineRecipeBuilder testMachine()
	{
		return new RecipeTestMachine.TestMachineRecipeBuilder(event);
	}
}
