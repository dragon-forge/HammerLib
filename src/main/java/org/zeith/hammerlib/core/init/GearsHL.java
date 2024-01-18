package org.zeith.hammerlib.core.init;

import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.annotations.*;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.core.items.ItemGear;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;
import org.zeith.hammerlib.proxy.HLConstants;

@SimplyRegister(
		prefix = "gears/",
		creativeTabs = @Ref(value = HLConstants.class, field = "HL_TAB")
)
public interface GearsHL
{
	@RegistryName("wooden")
	ItemGear WOODEN_GEAR = gear(TagsHL.Items.GEARS_WOODEN);
	
	@RegistryName("stone")
	ItemGear STONE_GEAR = gear(TagsHL.Items.GEARS_STONE);
	
	@RegistryName("copper")
	ItemGear COPPER_GEAR = gear(TagsHL.Items.GEARS_COPPER);
	
	@RegistryName("iron")
	ItemGear IRON_GEAR = gear(TagsHL.Items.GEARS_IRON);
	
	@RegistryName("gold")
	ItemGear GOLD_GEAR = gear(TagsHL.Items.GEARS_GOLD);
	
	@RegistryName("diamond")
	ItemGear DIAMOND_GEAR = gear(TagsHL.Items.GEARS_DIAMOND);
	
	@RegistryName("netherite")
	ItemGear NETHERITE_GEAR = gear(TagsHL.Items.GEARS_NETHERITE);
	
	static ItemGear gear(TagKey<Item> tag)
	{
		return new ItemGear(
				new Item.Properties(),
				tag
		);
	}
	
	static void recipes(RegisterRecipesEvent e)
	{
		e.shaped().shape(" s ", "sps", " s ").map('s', Tags.Items.RODS_WOODEN).map('p', ItemTags.PLANKS).result(WOODEN_GEAR).registerIf(WOODEN_GEAR::defaultRecipe);
		e.shaped().shape(" s ", "sgs", " s ").map('s', Tags.Items.COBBLESTONE).map('g', WOODEN_GEAR).result(STONE_GEAR).registerIf(STONE_GEAR::defaultRecipe);
		e.shaped().shape(" s ", "sgs", " s ").map('s', Tags.Items.INGOTS_COPPER).map('g', STONE_GEAR).result(COPPER_GEAR).registerIf(COPPER_GEAR::defaultRecipe);
		e.shaped().shape(" s ", "sgs", " s ").map('s', Tags.Items.INGOTS_IRON).map('g', COPPER_GEAR).result(IRON_GEAR).registerIf(IRON_GEAR::defaultRecipe);
		e.shaped().shape(" s ", "sgs", " s ").map('s', Tags.Items.INGOTS_GOLD).map('g', IRON_GEAR).result(GOLD_GEAR).registerIf(GOLD_GEAR::defaultRecipe);
		e.shaped().shape(" s ", "sgs", " s ").map('s', Tags.Items.GEMS_DIAMOND).map('g', GOLD_GEAR).result(DIAMOND_GEAR).registerIf(DIAMOND_GEAR::defaultRecipe);
		
		if(NETHERITE_GEAR.defaultRecipe())
		{
			var id = ForgeRegistries.ITEMS.getKey(NETHERITE_GEAR);
			e.register(id, new SmithingTransformRecipe(id,
					Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
					Ingredient.of(DIAMOND_GEAR),
					RecipeHelper.fromTag(Tags.Items.INGOTS_NETHERITE),
					new ItemStack(NETHERITE_GEAR)
			));
		}
	}
}