package org.zeith.hammerlib.api.crafting.impl;

import net.minecraft.item.ItemStack;
import org.zeith.hammerlib.api.crafting.ICraftingResult;

public class ItemStackResult
		implements ICraftingResult<ItemStack>
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