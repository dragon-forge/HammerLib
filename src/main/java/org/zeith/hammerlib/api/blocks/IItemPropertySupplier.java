package org.zeith.hammerlib.api.blocks;

import net.minecraft.item.Item;

public interface IItemPropertySupplier
{
	Item.Properties createItemProperties(Item.Properties props);
}