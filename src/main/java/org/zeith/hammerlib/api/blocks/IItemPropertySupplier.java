package org.zeith.hammerlib.api.blocks;

import net.minecraft.world.item.Item;

public interface IItemPropertySupplier
{
	Item.Properties createItemProperties(Item.Properties props);
}