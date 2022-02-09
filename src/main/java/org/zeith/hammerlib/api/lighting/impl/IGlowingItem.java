package org.zeith.hammerlib.api.lighting.impl;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.zeith.hammerlib.api.lighting.ColoredLight;

public interface IGlowingItem
{
	/**
	 * @param entity can be either {@link net.minecraft.world.entity.item.ItemEntity} or {@link net.minecraft.world.entity.LivingEntity}
	 *               that holds current item.
	 * @return the light produced, or null.
	 */
	ColoredLight produceColoredLight(Entity entity, ItemStack stack);

	static IGlowingItem fromStack(ItemStack stack)
	{
		if(!stack.isEmpty())
		{
			Block cache;
			Item item = stack.getItem();
			if(item instanceof IGlowingItem)
				return (IGlowingItem) item;
			else if((cache = Block.byItem(item)) instanceof IGlowingItem)
				return (IGlowingItem) cache;
		}
		return null;
	}
}