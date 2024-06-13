package org.zeith.hammerlib.core.recipes.replacers;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;


@SimplyRegister
public class WaterBottleReplacer
		implements IRemainingItemReplacer
{
	@RegistryName("water_bottle")
	public static final IRemainingItemReplacer REPLACER = new WaterBottleReplacer();
	
	@Override
	public ItemStack replace(CraftingInput container, int slot, ItemStack prevItem)
	{
		var stored = container.getItem(slot);
		
		if(stored.is(Items.POTION) && prevItem.isEmpty())
			return new ItemStack(Items.GLASS_BOTTLE);
		
		return prevItem;
	}
}