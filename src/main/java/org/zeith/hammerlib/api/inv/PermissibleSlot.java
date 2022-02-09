package org.zeith.hammerlib.api.inv;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PermissibleSlot
		extends Slot
{
	public PermissibleSlot(Container inv, int id, int x, int y)
	{
		super(inv, id, x, y);
	}

	@Override
	public boolean mayPlace(ItemStack stack)
	{
		return container.canPlaceItem(index, stack);
	}
}