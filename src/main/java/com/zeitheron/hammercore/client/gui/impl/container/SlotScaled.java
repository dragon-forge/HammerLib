package com.zeitheron.hammercore.client.gui.impl.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotScaled extends Slot
{
	protected int width, height;
	
	public SlotScaled(IInventory inventoryIn, int index, int xPosition, int yPosition, int width, int height)
	{
		super(inventoryIn, index, xPosition, yPosition);
		this.width = width;
		this.height = height;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return inventory.isItemValidForSlot(getSlotIndex(), stack);
	}
}