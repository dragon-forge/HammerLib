package org.zeith.hammerlib.core.init;

import lombok.var;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.annotations.*;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.core.items.ItemGear;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;
import org.zeith.hammerlib.proxy.HLConstants;

@SimplyRegister(prefix = "gears/")
public interface GearsHL
{
	@RegistryName("wooden")
	ItemGear WOODEN_GEAR = gear();
	
	@RegistryName("stone")
	ItemGear STONE_GEAR = gear();
	
	@RegistryName("copper")
	ItemGear COPPER_GEAR = gear();
	
	@RegistryName("iron")
	ItemGear IRON_GEAR = gear();
	
	@RegistryName("gold")
	ItemGear GOLD_GEAR = gear();
	
	@RegistryName("diamond")
	ItemGear DIAMOND_GEAR = gear();
	
	@RegistryName("netherite")
	ItemGear NETHERITE_GEAR = gear();
	
	static ItemGear gear()
	{
		return new ItemGear(new Item.Properties().tab(HLConstants.HL_TAB));
	}
	
	static void recipes(RegisterRecipesEvent e)
	{
		e.shaped()
				.shape(" s ", "sps", " s ")
				.map('s', Tags.Items.RODS_WOODEN)
				.map('p', ItemTags.PLANKS)
				.result(WOODEN_GEAR)
				.registerIf(WOODEN_GEAR::defaultRecipe);
		e.shaped()
				.shape(" s ", "sgs", " s ")
				.map('s', Tags.Items.COBBLESTONE)
				.map('g', WOODEN_GEAR)
				.result(STONE_GEAR)
				.registerIf(STONE_GEAR::defaultRecipe);
		e.shaped()
				.shape(" s ", "sgs", " s ")
				.map('s', Tags.Items.INGOTS_IRON)
				.map('g', STONE_GEAR)
				.result(IRON_GEAR)
				.registerIf(IRON_GEAR::defaultRecipe);
		e.shaped()
				.shape(" s ", "sgs", " s ")
				.map('s', Tags.Items.INGOTS_GOLD)
				.map('g', IRON_GEAR)
				.result(GOLD_GEAR)
				.registerIf(GOLD_GEAR::defaultRecipe);
		e.shaped()
				.shape(" s ", "sgs", " s ")
				.map('s', Tags.Items.GEMS_DIAMOND)
				.map('g', GOLD_GEAR)
				.result(DIAMOND_GEAR)
				.registerIf(DIAMOND_GEAR::defaultRecipe);
		
		if(NETHERITE_GEAR.defaultRecipe())
		{
			var id = ForgeRegistries.ITEMS.getKey(NETHERITE_GEAR);
			e.add(new SmithingRecipe(id,
					Ingredient.of(DIAMOND_GEAR),
					RecipeHelper.fromTag(Tags.Items.INGOTS_NETHERITE),
					new ItemStack(NETHERITE_GEAR)
			));
		}
	}
}