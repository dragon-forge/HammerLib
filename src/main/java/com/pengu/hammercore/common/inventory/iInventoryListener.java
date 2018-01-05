package com.pengu.hammercore.common.inventory;

import net.minecraft.item.ItemStack;

public interface iInventoryListener
{
	public void slotChange(int slot, ItemStack stack);
	
	public InventoryNonTile getInventory();
}