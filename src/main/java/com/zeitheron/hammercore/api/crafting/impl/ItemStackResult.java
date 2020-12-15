package com.zeitheron.hammercore.api.crafting.impl;

import com.zeitheron.hammercore.api.crafting.ICraftingResult;
import net.minecraft.item.ItemStack;

public class ItemStackResult implements ICraftingResult<ItemStack>
{
	protected ItemStack output;
	
	public ItemStackResult(ItemStack output)
	{
		this.output = output;
	}
	
	@Override
	public ItemStack getBaseOutput()
	{
		return output.copy();
	}
	
	@Override
	public Class<ItemStack> getType()
	{
		return ItemStack.class;
	}
}