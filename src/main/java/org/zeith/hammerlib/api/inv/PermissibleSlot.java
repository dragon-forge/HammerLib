package org.zeith.hammerlib.api.inv;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class PermissibleSlot
		extends Slot
{
	public PermissibleSlot(IInventory inv, int id, int x, int y)
	{
		super(inv, id, x, y);
	}

	@Override
	public boolean mayPlace(ItemStack stack)
	{
		return container.canPlaceItem(index, stack);
	}
}