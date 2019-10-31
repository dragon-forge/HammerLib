package com.zeitheron.hammercore.api.lighting.impl;

import com.zeitheron.hammercore.api.lighting.ColoredLight;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IGlowingItem
{
	/**
	 * @param entity
	 *            can be either {@link EntityItem} or {@link EntityLivingBase}
	 *            that holds current item.
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
			else if((cache = Block.getBlockFromItem(item)) instanceof IGlowingItem)
				return (IGlowingItem) cache;
		}
		return null;
	}
}