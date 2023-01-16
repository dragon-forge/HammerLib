package org.zeith.hammerlib.core.init;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.zeith.hammerlib.annotations.ProvideRecipes;
import org.zeith.hammerlib.api.IRecipeProvider;
import org.zeith.hammerlib.core.test.HammerLibRecipeExtension;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

@ProvideRecipes
public class RecipesHL
		implements IRecipeProvider
{
	@Override
	public void provideRecipes(RegisterRecipesEvent event)
	{
		var $ = event.extension(HammerLibRecipeExtension.class);
		if($ != null)
		{
			$.testMachine()
					.top(ItemTags.SAND, 1)
					.bottom(ItemTags.LOGS_THAT_BURN, 1)
					.result(new ItemStack(Items.GLASS))
					.register();
		}
		
		/*
		event.shaped()
				.shape("iri", "pop", "iri")
				.map('i', Tags.Items.INGOTS_IRON)
				.map('r', Tags.Items.DUSTS_REDSTONE)
				.map('p', TagsHL.Items.PISTONS)
				.map('o', Tags.Items.OBSIDIAN)
				.result(new ItemStack(BlockTestMachine.TEST_MACHINE))
				.register();
		 */
	}
}