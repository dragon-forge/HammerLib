package org.zeith.hammerlib.core.recipes.replacers;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.*;
import org.zeith.hammerlib.annotations.*;

@SimplyRegister
public class WaterBottleReplacer
		implements IRemainingItemReplacer
{
	@RegistryName("water_bottle")
	public static final IRemainingItemReplacer REPLACER = new WaterBottleReplacer();
	
	@Override
	public ItemStack replace(CraftingContainer container, int slot, ItemStack prevItem)
	{
		var stored = container.getItem(slot);
		
		if(stored.is(Items.POTION) && prevItem.isEmpty())
			return new ItemStack(Items.GLASS_BOTTLE);
		
		return prevItem;
	}
}