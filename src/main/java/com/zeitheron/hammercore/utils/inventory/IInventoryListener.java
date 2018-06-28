package com.zeitheron.hammercore.utils.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IInventoryListener
{
	void slotChange(int slot, ItemStack stack);
	
	default IInventory getInventory()
	{
		return null;
	}
}