package org.zeith.hammerlib.core.init;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Tags;
import org.zeith.hammerlib.annotations.ProvideRecipes;
import org.zeith.hammerlib.api.IRecipeProvider;
import org.zeith.hammerlib.core.test.TestPreferences;
import org.zeith.hammerlib.core.test.machine.BlockTestMachine;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

@ProvideRecipes
public class RecipesHL
		implements IRecipeProvider
{
	@Override
	public void provideRecipes(RegisterRecipesEvent event)
	{
		if(TestPreferences.enableTestMachine())
			event.shaped()
					.shape("iri", "pop", "iri")
					.map('i', Tags.Items.INGOTS_IRON)
					.map('r', Tags.Items.DUSTS_REDSTONE)
					.map('p', TagsHL.Items.PISTONS)
					.map('o', Tags.Items.OBSIDIAN)
					.result(new ItemStack(BlockTestMachine.TEST_MACHINE))
					.register();
	}
}