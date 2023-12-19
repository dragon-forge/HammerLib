package org.zeith.hammerlib.core.init;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.Tags;
import org.zeith.hammerlib.annotations.*;
import org.zeith.hammerlib.api.IRecipeProvider;
import org.zeith.hammerlib.core.recipes.*;
import org.zeith.hammerlib.core.test.HammerLibRecipeExtension;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;
import org.zeith.hammerlib.util.java.Cast;


@ProvideRecipes
@SimplyRegister
public class RecipesHL
		implements IRecipeProvider
{
	@RegistryName("shaped_hl_recipe")
	public static final RecipeSerializer<HLShapedRecipe> SHAPED_HL_SERIALIZER = new HLShapedRecipe.HLSerializer();
	
	@RegistryName("shapeless_hl_recipe")
	public static final RecipeSerializer<HLShapelessRecipe> SHAPELESS_HL_SERIALIZER = new HLShapelessRecipe.HLSerializer();
	
	@Override
	public void provideRecipes(RegisterRecipesEvent e)
	{
		var $ = e.extension(HammerLibRecipeExtension.class);
		if($ != null)
		{
			$.testMachine()
			 .top(ItemTags.SAND, 1)
			 .bottom(ItemTags.LOGS_THAT_BURN, 1)
			 .result(new ItemStack(Items.GLASS))
			 .registerIf(Cast.constantB(false));
		}
		
		GearsHL.recipes(e);
		
		e.shaped()
		 .shape("i i", " g ", " i ")
		 .map('i', Tags.Items.INGOTS_IRON)
		 .map('g', TagsHL.Items.GEARS_IRON)
		 .result(ItemsHL.WRENCH)
		 .registerIf(ItemsHL.WRENCH::defaultRecipe);
		
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